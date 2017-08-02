<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>${env.title}</title>
        <script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
        <style>
            html, body { margin:0; padding:0; font-family: sans-serif;}
            ul { margin: 5px; padding: 0 15px;}
            ul li { margin: 2px 0;}
            header { height: 70px; position: fixed; width: 100%; top: 0px; z-index: 9999;}
            header div { padding: 0 10px; }
            header div.banner { background-color: #1C4598; line-height: 45px; height: 45px; color: #FFF; }
            header div.remark { background-color: #E3E3E3; line-height: 25px; height: 25px; color: #000; font-size: 9pt; }

            #container { margin-top: 10px; padding: 0 10px;}

            .nav { position: fixed; overflow: auto; top: 70px; left: 0; width: 220px; height: 100%; list-style-type: none; margin: 0; padding: 0; background: #f7f7f7; font-size: 9pt; }
            .nav {float: left;}
            .nav ul { padding-left: 10px;}
            .nav ul li.ccat span { cursor: pointer; }
            .main { margin-top: 80px; padding-left: 230px; }
            .cpass { display: none; }
            .cbox { border: 1px dashed #eee; font-size: 14px; margin-bottom: 10px; }
            .cbox .ctitle { line-height: 30px; height: 30px; background-color: #eee; padding-left: 5px; color: #fff;}
            .cbox .error { background-color: #F52908; }
            .cbox .success { background-color: #1c4; }
            .cbox .cdata table { width: 100%; border-collapse:collapse; border-spacing: 0;  border: solid #ccc 1px; table-layout:fixed; word-wrap:break-word;}
            .cbox .cdata table td, .cbox .cdata table th { border-left: 1px solid #ccc; border-top: 1px solid #ccc; }
            .cbox .cdata table th { background-color: #dce9f9;  background-color: #F6F6F6; font-size: 12px; text-align: center}
            .cbox .cdata table td { font-size: 9pt; }

            .w-caution { background-color: #F52908; color: #FFF; }
            .w-info { color: #1C4598; }
            .w-green { color: #1c4; }
            .w-red { color: #F52908; }
        </style>
        <script>
            $(function(){
                $("#hidePassChk").click(function(){
                    !$(this).is(':checked') ? $(".cpass").fadeIn() : $(".cpass").fadeOut();
                });

                $(".ccat span").each(function(){
                    $(this).click(function(){
                        $(this).parent().find("ul").slideToggle();
                    });
                });
            });
        </script>
    </head>
    <body>
        <header>
            <div class="banner">${env.title} (v${env.version}) - Report </div>
            <div class="remark">
                <label>Scan Folder: ${env.scanningPath}</label>
                <div style="float: right; vertical-align:middle;">
                    <input type="checkbox" checked="true" id="hidePassChk" style="vertical-align:middle; margin-top: 1px;" /> Hide Passed
                </div>
            </div>
        </header>
        <div id="container">
            <div class="nav">
                <ul>
                    <#list dataMap?keys as key>
                        <li class="ccat"><span>${key} (<label class="w-red">${dataMap[key].failureNum}</label> / <label class="w-info">${dataMap[key].totalNum}</label></span>)
                            <ul>
                                <#list dataMap[key].data as data>
                                    <#assign navs = data.navigations>
                                    <#list navs?keys as navKey>
                                        <li class="<#if !navs[navKey]>cpass</#if>"><a href="#${data.category}-${navKey}" class="<#if navs[navKey]>w-red<#else>w-green</#if>">${navKey}</a></li>
                                    </#list>
                                </#list>
                            </ul>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="main">
                <#list dataMap?keys as key>
                    <div>
                        <div style="font-size: 24px; background-color: rgb(194, 205, 216)">${key}</div>
                        <#list dataMap[key].data as data>
                        <div class="cbox <#if data.hasError>cerror<#else>cpass</#if>">
                            <#if !data.plainText><a name="${data.category}-${data.fileName}"></a></#if>
                            <div class="ctitle <#if data.hasError>error<#else>success</#if>">${data.filePath}</div>
                            <div class="cdata">
                                <table>
                                    <tr>
                                        <th width="100px">Label</th>
                                        <th>Text</th>
                                        <th width="40%">Remarks</th>
                                    </tr>
                                    <#list data.labels as label>
                                    <tr>
                                        <td align="left"><#if data.plainText><a name="${data.category}-${label.label}"></a></#if>${label.label}</td>
                                        <td >${label.rawText}</td>
                                        <td>
                                            <#if label.hasError>
                                            <ul>
                                                <#list label.errorItems as errorItem>
                                                <li>
                                                    <b class="w-caution">${errorItem.errorWord}</b>:
                                                    ${errorItem.errorDescription}, suggested corrections: <font class="w-info">[${errorItem.suggestReplacements}]</font>
                                                </li>
                                                </#list>
                                            </ul>
                                            </#if>
                                        </td>
                                    </tr>
                                    </#list>
                                </table>
                            </div>
                        </div>
                        </#list>
                    </div>
                </#list>
                </div>
            </div>
        </div>
    </body>
</html>