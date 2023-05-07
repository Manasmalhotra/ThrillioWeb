package bgjobs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import dao.BookmarkDao;
import entities.WebLink;
import util.HttpConnect;
import util.IOUtil;

public class WebpageDownloaderTask implements Runnable{
    
	private static BookmarkDao dao=new BookmarkDao();
	private static final long TIME_FRAME=3000000000L; //3seconds
	private boolean downloadAll=false;
	
	public WebpageDownloaderTask(boolean downloadAll) {
		this.downloadAll=downloadAll;
	}

	
	ExecutorService downloadExecutor=Executors.newFixedThreadPool(5);
	
	private static class Downloader<T extends WebLink> implements Callable<T>{
		private T weblink;
		public Downloader(T weblink) {
			this.weblink=weblink;
		}
		@Override
		public T call(){
			// TODO Auto-generated method stub
			try {
				if(!weblink.getUrl().endsWith(".pdf")) {
					weblink.setDownloadStatus(WebLink.DownloadStatus.FAILED);
					String htmlPage=HttpConnect.download(weblink.getUrl());
				    weblink.setHtmlPage(htmlPage);
				    
				}
				else {
					weblink.setDownloadStatus(WebLink.DownloadStatus.NOT_ELIGIBLE);
				}
			}
			catch(MalformedURLException e) {
				e.printStackTrace();
			}
			catch(URISyntaxException e) {
				e.printStackTrace();
			}
			return weblink;
		}	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!Thread.currentThread().isInterrupted()) {
			//Get WebLinks
			List<WebLink> weblinks= getWebLinks();
			//Download Concurrently
		    if(weblinks!=null) {
		    	download(weblinks);
		    }
		    else {
		    	System.out.println("No new Web Links todownload!");
		    	break;
		    }
		    //Wait
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch blocks, 
				e.printStackTrace();
			}
		}
		downloadExecutor.shutdown();
	}
	
	private void download(List<WebLink>weblinks) {
		List<Downloader<WebLink>> tasks = getTasks(weblinks);
		List<Future<WebLink>> futures = new ArrayList<>();
	
		try {
			futures = downloadExecutor.invokeAll(tasks, TIME_FRAME, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (Future<WebLink> future : futures) {
			try {
				if (!future.isCancelled()) {
					WebLink weblink=future.get();
					String webPage=weblink.getHtmlPage();
					if(webPage!=null) {
						IOUtil.write(webPage,weblink.getId());
						weblink.setDownloadStatus(WebLink.DownloadStatus.SUCCESS);
						System.out.println("Download Success: "+weblink.getUrl());
					}
					else {
						System.out.println("Webpage not downloaded: "+weblink.getUrl());
					}
				} else {
					System.out.println("\n\nTask is cancelled --> " + Thread.currentThread());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<WebLink> getWebLinks(){
		List<WebLink> webLinks=null;
		if(downloadAll) {
			webLinks=dao.getAllWebLinks();
			downloadAll=false;
		}
		else {
			dao.getWebLinks(WebLink.DownloadStatus.NOT_ATTEMPTED);
		}
		return webLinks;
	}
private List<Downloader<WebLink> >getTasks(List<WebLink> weblinks){
      List<Downloader<WebLink> >tasks=new ArrayList<>();
      for(WebLink weblink: weblinks) {
    	  tasks.add(new Downloader<WebLink>(weblink));
      }
      return tasks;
}
}