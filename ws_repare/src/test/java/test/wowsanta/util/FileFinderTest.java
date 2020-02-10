package test.wowsanta.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wowsanta.util.FileFindHandler;
import com.wowsanta.util.FileFinder;

public class FileFinderTest {
	@Test
	public void test() {
		FileFinder finder = new FileFinder();
		finder.addDirectory("./old_version/ws_server_1.0.8_00_201611032059_debug");
		finder.addHandler(new FileFindHandler() {
			public boolean visit(File root, File file) {
				try {
					Path root_path = root.toPath();
					Path target_path = root.toPath().toAbsolutePath().getParent().getParent().getParent().getParent().resolve("ws_server/src/main/java");
					
					List<Path> path_list = new ArrayList<Path>();
					Path parent_path = file.toPath();//.getParent();
					System.out.println(root_path);
					while(parent_path != null && !root_path.equals(parent_path)){
						path_list.add(0,parent_path.getFileName());
						parent_path = parent_path.getParent();
					}
						
					
					for (Path path : path_list) {
						target_path = target_path.resolve(path);
					}
					
					System.out.println(file.getCanonicalPath());
					System.out.println(target_path.toAbsolutePath());
					
					Path parent_target = target_path.getParent();
					if(!parent_target.toFile().exists()) {
						parent_target.toFile().mkdirs();
					}
					Files.copy(file.toPath(), target_path);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
			
			public boolean condition(File file) {
				boolean compare = false;
				if( file.getName().endsWith(".java") || file.getName().endsWith(".xml")) {
					compare	= true; 
				}
				return compare;
			}
		});
		
		finder.run();
	}
}
