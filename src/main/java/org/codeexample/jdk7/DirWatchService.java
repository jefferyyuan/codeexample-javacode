package org.codeexample.jdk7;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Source: http://radar.oreilly.com/2011/09/java7-features.html
 * 
 */
public class DirWatchService
{
    private Path path = null;
    private WatchService watchService = null;

    private void init()
    {
        path = Paths.get("C:\\Temp\\temp\\");
        try
        {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        }
        catch (IOException e)
        {
            System.out.println("IOException" + e.getMessage());
        }
    }

    /**
     * The police will start making rounds
     */
    private void doRounds()
    {
        WatchKey key = null;
        while (true)
        {
            try
            {
                key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents())
                {
                    Kind<?> kind = event.kind();
                    System.out.println("Event on " + event.context().toString() + " is " + kind);
                }
            }
            catch (InterruptedException e)
            {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            boolean reset = key.reset();
            if (!reset)
                break;
        }
    }

    public static void main(
            String[] args)
    {
        DirWatchService police = new DirWatchService();
        police.init();
        police.doRounds();
    }
}
