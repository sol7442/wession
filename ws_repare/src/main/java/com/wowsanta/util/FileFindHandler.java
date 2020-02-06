package com.wowsanta.util;

import java.io.File;

public interface FileFindHandler {
	public boolean visit(File root, File file);
	public boolean condition(File file); 
}
