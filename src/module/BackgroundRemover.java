package module;

import java.io.File;

public class BackgroundRemover {



    public static void removeLatency(String local_path , String[] thumbList, long latency){

        BackWorker bw = new BackWorker(local_path, thumbList, latency);
        bw.run();

    }

    public static class BackWorker implements Runnable{
        long latency;
        String local_path;
        String[] thumbList;

        public BackWorker(String local_path, String[] thumbList, long _latency){
            this.local_path = local_path;
            this.thumbList = thumbList;
            latency = _latency;
        }

        @Override
        public void run() {
            try{
                Thread.sleep(latency);
                for(String thumb :thumbList){
                    try {
                        File f = new File(local_path + thumb);
                        //System.out.println("삭제 시도 : " + local_path + thumb);
                        f.delete();
                    }catch(Exception fe){
                        fe.printStackTrace();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }


}
