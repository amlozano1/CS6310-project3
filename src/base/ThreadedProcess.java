package base;

import java.util.logging.Level;

import exceptions.ThreadException;

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
	private Thread mRunningThread;

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
	public void pause() throws ThreadException {
		if (mRunningThread == null && mRunningThread.isAlive()) {
			throw new ThreadException("Thread has not been started");
		} else {
			synchronized (mPaused) {
				logWithThreadName(Level.INFO, "Pausing %s thread");
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
	public void resume() throws ThreadException {
		if (mRunningThread == null && mRunningThread.isAlive()) {
			throw new ThreadException("Thread has not been started");
		} else {
			synchronized (mPaused) {
				logWithThreadName(Level.INFO, "Resuming %s thread");
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
	public void start() throws ThreadException {
		if (mRunningThread == null || !mRunningThread.isAlive()) {
			String threadName = getThreadName();
			logWithThreadName(Level.INFO, "Starting %s thread");
			mRunningThread = threadName == null ? new Thread(getRunnableAction()) : new Thread(getRunnableAction(), threadName);
			mRunningThread.start();
		} else {
			throw new ThreadException("Thread already started");
		}
	}

	/**
	 * Stops the currently executing thread. Throws an exception if the thread
	 * has not been started.
	 * 
	 * @throws Exception Thrown if the thread has not been started.
	 */
	public void stop() throws ThreadException {
		if (mRunningThread == null && mRunningThread.isAlive()) {
			throw new ThreadException("Thread has not been started");
		} else {
			logWithThreadName(Level.INFO, "Stopping %s thread");
			mRunningThread.interrupt();
		}
	}
	
	protected void acknowledgeStopped() {
		mRunningThread = null;
	}

	/**
	 * Checks the pause state of the thread and waits if necessary.
	 * 
	 * @throws InterruptedException Thrown if the thread is interrupted while paused.
	 */
	protected void checkPaused() throws InterruptedException {
		synchronized (mPaused) {
			if (mPaused) {
				mPaused.wait();
			}
		}
	}
	
	/**
	 * Checks the stopped state of the thread
	 * 
	 * @return True if the thread is stopped, false otherwise
	 */
	protected boolean checkStopped() {
		return mRunningThread.isInterrupted();
	}
	
	/**
	 * Gets the name of the thread. Override to change the name of the thread.
	 * 
	 * @return The name of the thread
	 */
	protected String getThreadName() {
		return null;
	}
	
	/**
	 * Formats a message string, replacing %s with the thread name.
	 * 
	 * @param level The level the log message is set to
	 * @param message The message to log, with %s being replaces as the thread name
	 */
	protected void logWithThreadName(Level level, String message) {
		String threadName = getThreadName();
		if (threadName == null) {
			log(level, String.format(message, ""));
		} else {
			log(level, String.format(message, threadName));
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
