package com.example.wow.util.hot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HotFolderWatcher {
    private final Map<String, List<HotFolderListener>> hfListeners;
    private final Map<String,FileSystemWatcher> fsListeners;
    private final String rootFolder;
    private final long pollInterval;
    private final long quietPeriod;

    public HotFolderWatcher(String rootFolder, long pollInterval, long quietPeriod) {
        this.rootFolder = rootFolder;
        this.pollInterval = pollInterval;
        this.quietPeriod = quietPeriod;
        this.hfListeners = new ConcurrentHashMap<>();
        this.fsListeners = new ConcurrentHashMap<>();
    }

    public void stop() {
        for(FileSystemWatcher fsw:fsListeners.values()){
            fsw.stop();
        }
    }

    public void addListener(String path, HotFolderListener listener){
        var hotFolder = new File(rootFolder,path);
        if(hotFolder.exists() || hotFolder.mkdirs() && hotFolder.isDirectory()){
            if(hfListeners.containsKey(hotFolder.getPath())){
                hfListeners.get(hotFolder.getPath()).add(listener);
            }else{
                List<HotFolderListener> listeners = new ArrayList<>();
                listeners.add(listener);
                hfListeners.put(hotFolder.getPath(),listeners);
                var fsw = new FileSystemWatcher(true, Duration.ofMillis(pollInterval), Duration.ofMillis(quietPeriod), null);
                fsw.addSourceDirectory(hotFolder);
                fsw.addListener(changeListener());
                fsw.start();
                fsListeners.put(hotFolder.getPath(),fsw);
            }
            log.info("Created hot folder, watching for files here {}",hotFolder.getAbsolutePath());
        }
    }

    private FileChangeListener changeListener(){
        return changeSet -> changeSet.forEach(cfiles-> cfiles.forEach(cfile -> {
            if( cfile.getType().equals(ChangedFile.Type.MODIFY) ||
                cfile.getType().equals(ChangedFile.Type.ADD) &&
                !isLocked(cfile.getFile().toPath())) {
                    List<HotFolderListener> listenerList =  hfListeners.get(cfile.getFile().getParentFile().getPath());
                    if(listenerList != null){
                        listenerList.forEach( hfListener-> hfListener.onChange(cfile.getFile()));
                    }
            }
        }));
    }

    private boolean isLocked(Path path) {
        try (var ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}
