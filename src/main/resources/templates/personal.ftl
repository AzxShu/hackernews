<#include "header.ftl">
<div id="main">
    <div class="container" align="">
       <#-- 头像: <img alt="头像" src="${user.headUrl!}"/><br>
        用户名: ${user.name!} <br>
        密码: ${user.password!} <br>
        用户积分: ${user.salt!} <br>-->
        <div class="col-xs-9">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        个人信息
                    </h3>
                </div>
                <div class="panel-body">
                    <#--  <%
                      ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
                      UserBiz userBiz = (UserBiz)context.getBean("userBiz");
                      User user = userBiz.getUserById((Integer)session.getAttribute("userId"));

                      %>-->
                    <form id="form1" action="${contextPath}/" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <img alt="@zhangjianhao" class="avatar left" height="70" src="${user.headUrl!}" />
                            请上传新的头像<br/>
                        </div>
                        <input type="file" id="inputfile" accept="image/*" name="photoImg"><br/>

                        <div class="form-group">
                            <label for="name">用户名</label>
                            <input id="username" type="text" class="form-control" name="username" id="name" width="200px" height="80px" value="${user.name!}"
                                   placeholder="请输入名称"> <p class="help-block"><s:fielderror fieldName="username"></s:fielderror></p>
                        </div>

                        <dl class="form-group">
                            <dt><label for="user_profile_blog">邮箱</label></dt>
                            <dd><input id="email" type="email" class="form-control" id="user_profile_blog" name="email" size="30" value="${user.email!}" />
                                <p class="help-block"><s:fielderror fieldName="email"></s:fielderror></p>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt><label for="user_profile_company">密码</label></dt>
                            <dd><input class="form-control" id="password" name="password" size="30" type="password" width="200px" value="${user.password!} /></dd>
                        </dl>
                        <dl class="form-group">
                            <dt><label for="user_profile_location">重复密码</label></dt>
                            <dd><input class="form-control" id="confirm_password" name="confirm_password" size="30" type="password" width="200px" /></dd>
                        </dl>
                        <input type="submit" value="修改信息"> <s:fielderror fieldName="update-result"></s:fielderror>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<div id="main" >

    <div class="container" id="daily">
        <div class="jscroll-inner">
            <h3>分享列表</h3>
            <div class="daily">
                <#assign  cur_date =''/>
                <#list vos as vo >
                <#if cur_date != vo.news.createdDate?string("yyyy-MM-dd")>
                <#if vo_index gt 0 >
            </div> <#--   上一个要收尾 -->
            </#if>
            <#assign  cur_date =vo.news.createdDate?string("yyyy-MM-dd")/>
            <div class="posts">
                </#if>
                <div class="post">
                    <div class="votebar">
                        <#if  vo.like gt 0 >
                            <button class="click-like up pressed" data-id="${vo.news.id!}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount!}</span></button>
                        <#else>
                            <button class="click-like up" data-id="${vo.news.id!}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount!}</span></button>
                        </#if>
                        <#if vo.like < 0>
                            <button class="click-dislike down pressed" data-id="${vo.news.id!}" title="反对"><i class="vote-arrow"></i></button>
                        <#else>
                            <button class="click-dislike down" data-id="${vo.news.id!}" title="反对"><i class="vote-arrow"></i></button>
                        </#if>
                    </div>
                    <div class="content">
                        <div >
                            <img class="content-img" src="${vo.news.image!}" alt="">
                        </div>
                        <div class="content-main">
                            <h3 class="title">
                                <a target="_blank" rel="external nofollow" href="${contextPath}/news/${vo.news.id!}">${vo.news.title!}</a>
                            </h3>
                            <div class="meta">
                                ${vo.news.link!}
                                <span>
                                            <i class="fa icon-comment"></i> ${vo.news.commentCount!}
                                        </span>
                            </div>
                        </div>
                    </div>

                    <div class="user-info">
                        <div class="user-avatar">
                            <a href="${contextPath!}/user/$!{vo.user.id}/"><img width="32" class="img-circle" src="${vo.user.headUrl}"></a>
                        </div>
                    </div>
                    <div class="subject-name">来自 <a href="${contextPath!}/user/${vo.user.id}/">${vo.user.name}</a></div>
                    <p>
                        <#-- <a href="${contextPath!}/personal/update?op=${vo.news.id}">修改</a>-->
                        <li class="js-update">
                            <a href="${contextPath!}/personal/delete?op=${vo.news.id}">删除</a> ||
                            <a href="${contextPath!}/personal/update?op=${vo.news.id}">修改</a></li>
                    </p>
                </div>
                <#if vo_index == vos?size >

            </div>  <#--最后有个元素要收尾 -->
            </#if>
            </#list>
        </div>
    </div>
</div>
</div>
</div>
</div>
</div>
<#--<jsp:include page="/pages/bottom.jsp"/>-->
</body>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript">
    $(function(){
        $("#form1").validate({
            rules:{
                username:{
                    required:true
                },
                email:{
                    required:true,
                    email:true
                },
                password:{
                    rangelength:[6,20]
                },
                confirm_password:{
                    equalTo:"#password"
                }
            },
            messages:{
                username:{
                    required:"必填"
                },
                email:{
                    required:"必填",
                    email:"E-Mail格式不正确"
                },
                password:{
                    rangelength: $.validator.format("密码最小长度:{0}, 最大长度:{1}。")
                },
                confirm_password:{
                    equalTo:"两次密码输入不一致"
                }
            }
        });
    });
</script>
<style type="text/css">
    .error{
        color:red;
    }
</style>
<#include "footer.ftl">
