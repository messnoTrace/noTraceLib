package com.notrace.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetManager {

	public static NetManager instance;
	private NetManager (){};
	
	
	ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2, 20, 1,TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	
	public void addTask(Runnable runnable){
		if(runnable==null){
			threadPoolExecutor.execute(runnable);
		}
	}
	
	public static NetManager getInstance(){
		if(instance==null){
			instance=new NetManager();
		}
		return instance;
	}
	
	
}
