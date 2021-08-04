package com.example.wow.util.hot;

import com.example.wow.config.HotFolderConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@ExtendWith(SpringExtension.class)
@Scope("test")
@SpringJUnitConfig({HotFolderConfig.class})
class HotFolderWatcherTest {

  HotFolderWatcher hotFolderWatcher;
  File newFile;

  @Autowired
  HotFolderConfig config;

  @BeforeEach
  void before() throws IOException {
    MockitoAnnotations.initMocks(this);
    File resourceFolder = ResourceUtils.getFile("classpath:");
    File hotFolderBase = new File(resourceFolder,"hf");
    hotFolderBase.mkdirs();
    newFile = new File(new File(hotFolderBase,"event"), UUID.randomUUID() + ".json");
    hotFolderWatcher = new HotFolderWatcher( hotFolderBase.getAbsolutePath(),500,300);
    hotFolderWatcher.addListener("event", Assertions::assertNotNull);
  }

  @Test
  void onChangeListenerTest() throws IOException {
    AtomicBoolean loop = new AtomicBoolean(true);
    hotFolderWatcher.addListener("event", file -> {
      Assertions.assertEquals(file.getAbsolutePath(),newFile.getAbsolutePath());
      hotFolderWatcher.stop();
      loop.getAndSet(false);
    });
    newFile.createNewFile();
    while(Boolean.TRUE.equals(loop.get())){}
  }

  @Test
  void onConfigPreDestroyTest() {
    Assertions.assertDoesNotThrow(()->this.config.onDestroy());
  }

}
