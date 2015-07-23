package eu.equalparts.cardbase.testutils;

import java.io.File;

public class TestFile extends File implements AutoCloseable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3716372675161543906L;

	public TestFile(String pathname) {
		super(pathname);
	}

	@Override
	public void close() throws Exception {
		delete();
	}
	
	
	
}
