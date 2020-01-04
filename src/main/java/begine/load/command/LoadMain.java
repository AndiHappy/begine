package begine.load.command;

import begine.load.Page;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.jvm.hotspot.debugger.NoSuchSymbolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadMain {

    public String url = "https://www.230book.com/book/8563/1570231.html";

    private String currentResult;
    private String[] currentResultArray;
    private Page curpage ;

    private String nextResult;


    public BufferedReader  sc = new BufferedReader(new InputStreamReader( System.in ) ) ;


    ////////////////////////////////////////////////////////////////
    private  int curindex = 0;
    private  int curlength = 0;
    private  boolean begineOutput = false;
    ////////////////////////////////////////////////////////////////


    public LoadMain() {
    }

    public static void main(String[] args) {
        LoadMain main = new LoadMain();
        main.cmd();
    }

    // command 命令行模式
    private void cmd() {
        String cmd = null;
        while(!"exit".equalsIgnoreCase(cmd)){
            try {
                cmd = sc.readLine();
                dealCmd(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void dealCmd(String cmd) {
        switch (cmd){
            case "load":
                loadurl(this.url);
                return;
            case "ro":
                dealContentValue();
                return;
            case "o1":
                outputControl();
                return;
            case "":
                continueOutput();
                return;
            case "ls":
                lsPage();
                return;
            default:
                System.out.println("CMD: " + cmd);
                System.out.println("not support method ！");
                return;
        }
    }

    private void lsPage() {
       Page curpage =  this.getCurpage();
       String title = curpage.getTitle();
       Document document =  curpage.getDoc();
       Elements alink =  document.select(ShellConstant.nextSelector);
       if(alink != null && alink.hasText()){
           Element nextElement = alink.first();
           String links = nextElement.attr("href");
           if(StringUtils.isNotEmpty(links)){

           }
       }

    }

    private void continueOutput() {
        if(this.begineOutput && this.getCurrentResultArray().length > 10
                && this.curindex < this.getCurrentResultArray().length){
            outputControl();
        }

    }

    private void dealContentValue() {
        if(this.getCurrentResult() != null && this.getCurrentResult().length() > 100){
            String content = this.getCurrentResult();
            String[] contentArrray = content.split("[，|。|？]");
            if(content.length() > 10){
                this.setCurrentResultArray(contentArrray);
                System.out.println("real ready！");
            }

        }
    }

    // control output content
    private void outputControl() {
       if(this.begineOutput){
           String value = this.currentResultArray[this.curindex];
           if(value.length() > 80){
               System.out.println(value.substring(this.curlength,this.curlength+80));
               this.curlength+=80;
           }else{
               System.out.println(value);
               this.curlength = 0;
               this.curindex++;
           }

       }else{
           this.begineOutput=true;
           String value = this.currentResultArray[this.curindex];
           if(value.length() > 80){
               System.out.println(value.substring(this.curlength,this.curlength+80));
               this.curlength+=80;
           }else{
               System.out.println(value);
               this.curlength = 0;
               this.curindex++;
           }
       }
    }

    // load url
    private void loadurl(String url) {
        Page page = new Page(this.url);
        page.iniPageContent();
        page.iniPageTitle();
        String result =  page.getContentText();
        if(result != null && result.length() > 100){
            this.setCurrentResult(result);
            this.setCurpage(page);
            System.out.println("ready!");
        }
    }

    public String getCurrentResult() {
        return currentResult;
    }

    public void setCurrentResult(String currentResult) {
        this.currentResult = currentResult;
    }

    public Page getCurpage() {
        return curpage;
    }

    public void setCurpage(Page curpage) {
        this.curpage = curpage;
    }

    public String[] getCurrentResultArray() {
        return currentResultArray;
    }

    public void setCurrentResultArray(String[] currentResultArray) {
        this.currentResultArray = currentResultArray;
    }
}
