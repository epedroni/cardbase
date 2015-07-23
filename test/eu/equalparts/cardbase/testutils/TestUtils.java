package eu.equalparts.cardbase.testutils;

public class TestUtils {

	public static TestFile createValidTestFile(String fileName) throws Exception {
		TestFile testFile = new TestFile(fileName);
		if (!testFile.exists()) {
			if (testFile.createNewFile()) {
				if (testFile.canWrite()) {
					return testFile;
				} else {
					throw new IllegalArgumentException("Cannot write to " + testFile.getAbsolutePath() + ", aborting...");
				}
			} else {
				throw new IllegalArgumentException(testFile.getAbsolutePath() + " could not be created, aborting...");
			}
		} else {
			throw new IllegalArgumentException(testFile.getAbsolutePath() + " already exists, aborting...");
		}
	}
	
	
	
}
