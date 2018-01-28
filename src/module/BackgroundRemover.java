package module;

import java.io.File;

public class BackgroundRemover {



    public static void removeLatency(String path, long latency){

        BackWorker bw = new BackWorker(path, latency);
        bw.run();

    }

    public static class BackWorker implements Runnable{
        long latency;
        String path;

        public BackWorker(String _path, long _latency){
            path = _path;
            latency = _latency;
        }

        @Override
        public void run() {
            try{
                Thread.sleep(latency);
                File f = new File(path);
                f.delete();
            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }


}
