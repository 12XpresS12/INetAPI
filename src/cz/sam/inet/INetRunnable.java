package cz.sam.inet;

public abstract class INetRunnable implements Runnable {
	
	private boolean isRunning = true;
	private Thread thread;
	
	@Override
	public void run() {
		while(this.isRunning) {
			this.update();
		}
	}
	
	public abstract void update();
	
	public void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void stop() {
		this.isRunning = false;
		this.thread.interrupt();
	}
	
}
