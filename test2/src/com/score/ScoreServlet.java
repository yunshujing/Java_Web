package com.score;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 成绩处理Servlet
 * 功能：接收前端表单数据 → 校验合法性 → 计算总分/平均分/等级 → 输出结果页面
 */
@WebServlet("/ScoreServlet")
public class ScoreServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求和响应的编码，防止中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ---------- 1. 获取前端传来的数据 ----------
        String studentId = request.getParameter("studentId");
        String studentName = request.getParameter("studentName");
        String[] hobbies = request.getParameterValues("hobbies");
        String javaScoreStr = request.getParameter("javaScore");
        String webFrontScoreStr = request.getParameter("webFrontScore");
        String webServerScoreStr = request.getParameter("webServerScore");

        // ---------- 2. 数据合法性校验 ----------
        StringBuilder errorMsg = new StringBuilder();

        // 校验学号
        if (studentId == null || studentId.trim().isEmpty()) {
            errorMsg.append("学号不能为空！<br>");
        }

        // 校验姓名
        if (studentName == null || studentName.trim().isEmpty()) {
            errorMsg.append("姓名不能为空！<br>");
        }

        // 校验三门成绩：必须为数字且在 0~100 之间
        double javaScore = 0, webFrontScore = 0, webServerScore = 0;
        javaScore = parseAndValidateScore(javaScoreStr, "Java成绩", errorMsg);
        webFrontScore = parseAndValidateScore(webFrontScoreStr, "Web前端成绩", errorMsg);
        webServerScore = parseAndValidateScore(webServerScoreStr, "Web服务端成绩", errorMsg);

        // 如果存在校验错误，输出错误提示页面并返回
        if (errorMsg.length() > 0) {
            outputErrorPage(out, errorMsg.toString());
            return;
        }

        // ---------- 3. 计算总分、平均分 ----------
        double totalScore = javaScore + webFrontScore + webServerScore;
        double avgScore = totalScore / 3.0;

        // ---------- 4. 根据平均分判断等级 ----------
        String grade = getGrade(avgScore);

        // ---------- 5. 拼接兴趣爱好字符串 ----------
        String hobbyStr = "未选择";
        if (hobbies != null && hobbies.length > 0) {
            hobbyStr = String.join("、", hobbies);
        }

        // ---------- 6. 找出最高分和最低分科目 ----------
        String maxSubject = getMaxSubject(javaScore, webFrontScore, webServerScore);
        String minSubject = getMinSubject(javaScore, webFrontScore, webServerScore);

        // ---------- 7. 输出结果页面 ----------
        outputResultPage(out, studentId.trim(), studentName.trim(), hobbyStr,
                javaScore, webFrontScore, webServerScore,
                totalScore, avgScore, grade, maxSubject, minSubject);
    }

    /**
     * 解析并校验成绩，返回 double 值；如果格式有误或超范围，追加错误信息
     */
    private double parseAndValidateScore(String scoreStr, String fieldName, StringBuilder errorMsg) {
        if (scoreStr == null || scoreStr.trim().isEmpty()) {
            errorMsg.append(fieldName).append("不能为空！<br>");
            return 0;
        }
        try {
            double score = Double.parseDouble(scoreStr.trim());
            if (score < 0 || score > 100) {
                errorMsg.append(fieldName).append("必须在 0~100 之间！当前值：").append(score).append("<br>");
            }
            return score;
        } catch (NumberFormatException e) {
            errorMsg.append(fieldName).append("格式不正确，请输入数字！<br>");
            return 0;
        }
    }

    /**
     * 根据平均分返回等级
     */
    private String getGrade(double avg) {
        if (avg >= 90) return "优秀";
        if (avg >= 80) return "良好";
        if (avg >= 70) return "中等";
        if (avg >= 60) return "及格";
        return "不及格";
    }

    /**
     * 返回最高分科目名称
     */
    private String getMaxSubject(double java, double front, double server) {
        if (java >= front && java >= server) return "Java";
        if (front >= java && front >= server) return "Web前端";
        return "Web服务端";
    }

    /**
     * 返回最低分科目名称
     */
    private String getMinSubject(double java, double front, double server) {
        if (java <= front && java <= server) return "Java";
        if (front <= java && front <= server) return "Web前端";
        return "Web服务端";
    }

    /**
     * 输出错误提示页面，并提供返回按钮
     */
    private void outputErrorPage(PrintWriter out, String errorHtml) {
        String gradeColor = "#e74c3c";
        out.println("<!DOCTYPE html>");
        out.println("<html lang='zh-CN'><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>提交出错</title>");
        out.println("<style>");
        out.println(getCommonStyle());
        out.println("</style></head><body>");
        out.println("<div class='container'>");
        out.println("  <h1 style='color:" + gradeColor + "'>提交出错</h1>");
        out.println("  <div class='error-box'>" + errorHtml + "</div>");
        out.println("  <a href='score.html' class='btn-back'>返回重新填写</a>");
        out.println("</div>");
        out.println("</body></html>");
    }

    /**
     * 输出成绩结果展示页面
     */
    private void outputResultPage(PrintWriter out,
                                  String id, String name, String hobbies,
                                  double java, double front, double server,
                                  double total, double avg, String grade,
                                  String maxSubject, String minSubject) {

        // 根据等级选择主题色
        String gradeColor;
        switch (grade) {
            case "优秀": gradeColor = "#27ae60"; break;
            case "良好": gradeColor = "#2980b9"; break;
            case "中等": gradeColor = "#f39c12"; break;
            case "及格": gradeColor = "#e67e22"; break;
            default:     gradeColor = "#e74c3c"; break;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='zh-CN'><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>成绩查询结果</title>");
        out.println("<style>");
        out.println(getCommonStyle());
        out.println("</style></head><body>");
        out.println("<div class='container'>");

        // 标题
        out.println("<h1>成绩查询结果</h1>");

        // 基本信息卡片
        out.println("<div class='card'>");
        out.println("  <div class='card-title'>基本信息</div>");
        out.println("  <div class='info-row'><span class='label'>学　号：</span><span>" + id + "</span></div>");
        out.println("  <div class='info-row'><span class='label'>姓　名：</span><span>" + name + "</span></div>");
        out.println("  <div class='info-row'><span class='label'>兴趣爱好：</span><span>" + hobbies + "</span></div>");
        out.println("</div>");

        // 成绩明细卡片
        out.println("<div class='card'>");
        out.println("  <div class='card-title'>成绩明细</div>");
        out.println("  <table>");
        out.println("    <tr><th>科目</th><th>成绩</th><th>备注</th></tr>");
        out.println("    <tr><td>Java</td><td>" + formatScore(java) + "</td><td>" + getScoreRemark(java) + "</td></tr>");
        out.println("    <tr><td>Web前端</td><td>" + formatScore(front) + "</td><td>" + getScoreRemark(front) + "</td></tr>");
        out.println("    <tr><td>Web服务端</td><td>" + formatScore(server) + "</td><td>" + getScoreRemark(server) + "</td></tr>");
        out.println("  </table>");
        out.println("</div>");

        // 汇总统计卡片
        out.println("<div class='card'>");
        out.println("  <div class='card-title'>汇总统计</div>");
        out.println("  <div class='stat-grid'>");
        out.println("    <div class='stat-item'><div class='stat-value'>" + formatScore(total) + "</div><div class='stat-label'>总分</div></div>");
        out.println("    <div class='stat-item'><div class='stat-value'>" + String.format("%.1f", avg) + "</div><div class='stat-label'>平均分</div></div>");
        out.println("    <div class='stat-item'><div class='stat-value' style='color:" + gradeColor + "'>" + grade + "</div><div class='stat-label'>等级</div></div>");
        out.println("  </div>");
        out.println("  <div class='info-row' style='margin-top:12px'><span class='label'>最强科目：</span><span style='color:#27ae60;font-weight:600'>" + maxSubject + "</span></div>");
        out.println("  <div class='info-row'><span class='label'>薄弱科目：</span><span style='color:#e74c3c;font-weight:600'>" + minSubject + "</span></div>");
        out.println("</div>");

        // 返回按钮
        out.println("<a href='score.html' class='btn-back'>返回继续录入</a>");

        out.println("</div></body></html>");
    }

    /**
     * 格式化成绩显示：整数就不显示小数点，否则保留一位
     */
    private String formatScore(double score) {
        if (score == (int) score) {
            return String.valueOf((int) score);
        }
        return String.format("%.1f", score);
    }

    /**
     * 为单科成绩生成备注评价
     */
    private String getScoreRemark(double score) {
        if (score >= 90) return "优秀";
        if (score >= 80) return "良好";
        if (score >= 70) return "中等";
        if (score >= 60) return "及格";
        return "<span style='color:#e74c3c;font-weight:600'>不及格</span>";
    }

    /**
     * 返回错误页面和结果页面共用的 CSS 样式
     */
    private String getCommonStyle() {
        return "* { margin:0; padding:0; box-sizing:border-box; }"
            + "body { font-family:'Microsoft YaHei','PingFang SC',sans-serif;"
            + "  background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);"
            + "  min-height:100vh; display:flex; justify-content:center; align-items:center; padding:20px; }"
            + ".container { background:#fff; border-radius:16px; box-shadow:0 20px 60px rgba(0,0,0,.15);"
            + "  padding:40px; width:100%; max-width:560px; }"
            + "h1 { text-align:center; color:#333; font-size:22px; margin-bottom:24px;"
            + "  padding-bottom:12px; border-bottom:2px solid #667eea; }"
            + ".card { background:#f8f9fc; border-radius:10px; padding:20px; margin-bottom:16px; }"
            + ".card-title { font-size:15px; font-weight:700; color:#667eea; margin-bottom:12px; }"
            + ".info-row { display:flex; padding:5px 0; font-size:14px; }"
            + ".info-row .label { color:#888; min-width:80px; }"
            + "table { width:100%; border-collapse:collapse; font-size:14px; }"
            + "th { background:#667eea; color:#fff; padding:8px 12px; text-align:center; }"
            + "td { padding:8px 12px; text-align:center; border-bottom:1px solid #eee; }"
            + "tr:hover td { background:#eef1ff; }"
            + ".stat-grid { display:flex; gap:12px; }"
            + ".stat-item { flex:1; text-align:center; background:#fff; border-radius:8px; padding:14px 8px; }"
            + ".stat-value { font-size:26px; font-weight:700; color:#333; }"
            + ".stat-label { font-size:12px; color:#999; margin-top:4px; }"
            + ".btn-back { display:block; text-align:center; margin-top:20px; padding:12px;"
            + "  background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; text-decoration:none;"
            + "  border-radius:8px; font-size:15px; font-weight:600; transition:opacity .3s; }"
            + ".btn-back:hover { opacity:.9; }"
            + ".error-box { background:#fff5f5; border:1px solid #fed7d7; border-radius:8px;"
            + "  padding:16px; color:#e74c3c; font-size:14px; line-height:1.8; margin-bottom:16px; }";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET 请求直接重定向到表单页面
        response.sendRedirect("score.html");
    }
}
