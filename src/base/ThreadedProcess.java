package base;

import java.util.logging.Level;

/**
 * ThreadedProcess provides some core functionality for classes with
 * concurrent actions that can be started, stopped, paused, and resumed.
 * 
 * @author Tyler Benfield
 *
 */
public abstract class ThreadedProcess {

	/**
	 * Stores a reference to the currently executing thread. Once the thread is
	 * stopped, this variable is set back to null.
	 */
	protected Thread mRunningThread;

	/**
	 * Holds the current pause state. If true, the thread should pause at the
	 * next available opportunity.
	 */
	protected Boolean mPaused = false;

	/**
	 * Pauses the underlying thread if it is executing. Throws an exception if
	 * the thread has not been started.
	 * 
	 * @throws Exception Thrown if the thread has not been started.
	 */
	public void pause() throws Exception {
		if (mRunningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized (mPaused) {
				log(Level.INFO, "Pausing thread");
				mPaused = true;
			}
		}
	}

	/**
	 * Resumes the underlying thread if it is paused. Throws an exception if the
	 * thread has not been started.
	 * 
	 * @throws Exception Thrown if the thread has not been started.
	 */
	public void resume() throws Exception {
		if (mRunningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized (mPaused) {
				log(Level.INFO, "Resuming thread");
				Boolean toNotify = mPaused;
				mPaused = false;
				toNotify.notify();
			}
		}
	}

	/**
	 * Starts the the Runnable from getRunnableAction() on a new thread and begins the pause/resume/stop process.
	 * 
	 * @throws Exception Thrown if the thread has already been started.
	 */
	public void start() throws Exception {
		if (mRunningThread == null) {
			log(Level.INFO, "Starting thread");
			mRunningThread = new Thread(getRunnableAction());
			mRunningThread.start();
		} else {
			throw new Exception("Thread already started");
		}
	}

	/**
	 * Stops the currently executing thread. Throws an exception if the thread
	 * has not been started.
	 * 
	 * @throws Exception Thrown if the thread has not been started.
	 */
	public void stop() throws Exception {
		if (mRunningThread == null) {
			log(Level.INFO, "Stopping thread");
			throw new Exception("Thread has not been started");
		} else {
			mRunningThread.interrupt();
		}
	}

	/**
	 * Checks the pause state of the thread and waits if necessary.
	 * 
	 * @throws InterruptedException Thrown if the thread is interrupted while paused.
	 */
	protected synchronized void checkPaused() throws InterruptedException {
		synchronized (mPaused) {
			if (mPaused) {
				mPaused.wait();
			}
		}
	}

	/**
	 * Should be overridden to return a Runnable to execute in the thread when
	 * start() is called.
	 * 
	 * @return Runnable that will be executed on the underlying thread.
	 */
	protected abstract Runnable getRunnableAction();
	
	/**
	 * Should be overridden to log a message using the logging mechanism of the implementing class.
	 * Override with no method body to ignore log messages 
	 * 
	 * @param level The level the log message is set to
	 * @param message The message to log
	 */
	protected abstract void log(Level level, String message);
}
