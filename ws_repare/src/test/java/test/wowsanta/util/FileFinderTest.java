package test.wowsanta.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.wowsanta.util.FileFindHandler;
import com.wowsanta.util.FileFinder;

public class FileFinderTest {
	@Test
	public void test() {
		FileFinder finder = new FileFinder();
		finder.addDirectory("./old_version/ws_server_1.0.8_00 (1).src");
		finder.addHandler(new FileFindHandler() {
			public boolean visit(File root, File file) {
				try {
					System.out.println("file : " + file.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
			
			public boolean condition(File file) {
				return file.getName().endsWith(".java") ? true:false;
			}
		});
		
		finder.run();
	}
}
