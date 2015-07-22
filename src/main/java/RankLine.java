import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by g1a@pdl on 2015/7/3.
 */
public class RankLine {

    Site site = Site.me().setRetryTimes(2).setTimeOut(20000).setSleepTime(5000).setDomain("tianchi.com")
            .setUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    File csv = new File("./rank/rank_" + DateFormat.getDateInstance().format(new Date()) + ".csv");

    public void pholcus(List<String> urls) {
        Spider.create(new PageProcessor() {
            public void process(Page page) {
                List<String> rank = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div/p/text()").all();
                List<String> teamName = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div[2]/div/p/text()").all();
                List<String> member = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div[2]/div/div/div/text()").all();
                List<String> organize = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div[3]/div/p[2]/text()").all();
                List<String> score = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div[4]/text()").all();
                List<String> date = page.getHtml().xpath("html/body/div[2]/div[3]/div[2]/div/ul[3]/li/div[5]/text()").all();

                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv, true), "GBK"));
                    //bw.write("rank,team,member,organizew,score,date");
                    //bw.newLine();
                    for (int i = 0; i < rank.size(); i++) {
                        if (teamName.get(i).contains(",")) {
                            teamName.set(i, teamName.get(i).replace(",", ""));
                        }
                        bw.write(rank.get(i) + "," + teamName.get(i) + "," + member.get(i) + ","
                                + organize.get(i) + ","
                                + score.get(i) + ","
                                + date.get(i));
                        bw.newLine();
                    }
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public Site getSite() {
                return site;
            }
        }).addPipeline(new Pipeline() {
            public void process(ResultItems resultItems, Task task) {

            }
        }).startUrls(urls).thread(1).run();
    }

    public static void main(String[] args) {
        List<String> urls = new ArrayList<String>();

        for (int i = 1; i <= 10; i++) {
            urls.add("http://tianchi.aliyun.com/competition/rankingList.htm?season=1&raceId=3&pageIndex=" + i);
        }
        new RankLine().pholcus(urls);
    }
}
