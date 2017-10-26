import java.io.*;
import java.net.URL;

public class Tools {

    //   private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

    public static String getHTMLContent(String url) {
        return getHTMLContent(url, "utf-8");
    }

    public static String getHTMLContent(String url, String encoding) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),encoding));
            StringBuilder html = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                html.append(line).append("\n");
                line = reader.readLine();
            }
            String content = TextExtract.parse(html.toString());
            return content;
        } catch (Exception e) {
            //   LOG.debug("解析URL失败：" + url, e);
        }
        return null;
    }
    public static void copyFile(InputStream in, File outFile){
        OutputStream out = null;
        try {
            byte[] data=readAll(in);
            out = new FileOutputStream(outFile);
            out.write(data, 0, data.length);
            out.close();
        } catch (IOException ex) {
            //   LOG.error("文件操作失败",ex);
        } finally {
            try {
                if(in!=null){
                    in.close();
                }
            } catch (IOException ex) {
                //     LOG.error("文件操作失败",ex);
            }
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException ex) {
                //  LOG.error("文件操作失败",ex);
            }
        }
    }

    public static byte[] readAll(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            for (int n; (n = in.read(buffer)) > 0;) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            //    LOG.error("读取失败", e);
        }

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream("c:\\out.txt");
            fos.write(out.toByteArray());
        }
        catch(Exception e)
        {
            ;
        }


        return out.toByteArray();
    }
}
