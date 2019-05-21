<#include "header.ftl">
<div id="main" >
    <div class="container" id="daily">
        <div class="jscroll-inner">
            <div>
                <form id="form1" action="${contextPath}/news_update?newsid=${news.id}" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <h3>---新闻修改---</h3>
                    <br>
                    <br>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">新闻图片</label>
                        <div class="js-image-container col-sm-10">
                            <img src="${news.image}" width="200" height="150">
                        <#--<input type="file" class="btn btn-info btn-upload js-upload-btn" style="diplay:inline-block;position:relative;">-->
                        </div>
                    </div>

                    <div class="form-group"><label class="col-sm-2 control-label">分类</label><div class="col-sm-10">
                            <select name="category" class="js-fenlei form-control">
                                <option value="${news.category}">${news.category}</option><option value="财经">财经</option> <option value="娱乐">娱乐</option>
                                <option value="电竞">电竞</option><option value="时政">时政</option><option value="科技">科技</option>
                                </select></div></div>
                    <div class="form-group"><label class="col-sm-2 control-label">标题</label><div class="col-sm-10"><input name="title" class="js-title form-control" type="text" value="${news.title}"></div></div>
                    <div class="form-group"><label class="col-sm-2 control-label">链接</label><div class="col-sm-10"><input name="link" class="js-link form-control" type="text" value="${news.link}"></div></div>
                    <div class="form-group">
                        <div class="col-lg-10 col-lg-offset-2">
                            <input type="submit" value="提交" class="js-submit btn btn-default btn-info">
                            </div>
                        </div>
                </div>
                </form>
        </div>
    </div>
</div>

</div>
<#if pop??>
    <script>
        window.loginpop = ${(pop==0)?c};
    </script>
</#if>
<#include "footer.ftl">
