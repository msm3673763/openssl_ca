var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var Grid = require('../../widget/other/grid');
var CertForm = require('./certForm');
var DownloadPanel = require('./downloadPanel');
var PermissionLink = require('../../widget/other/permissionLink');
var PermissionButton = require('../../widget/other/permissionButton');

module.exports = React.createClass({
    getInitialState:function () {
        return{
        }
    },
    _query: function() {
        this.refs.grid.load({
            domainName: this.refs.domainName.getValue()
        });
    },
    _add: function() {
        var me = this;
        UcsmyIndex.openChildrenPage(CertForm, function(refPanel) {
            refPanel.init(me.props.type, function(){
                me.refs.grid.reload();
            });
        });
    },
    _download: function(data) {
        this.refs.download.init(data);
    },
    _delete: function(item) {
        var me = this;
        UcsmyIndex.confirm("确认", "确定删除该证书？", function() {
            $.ajax({
                url: "cert/delete",
                type: "post",
                async: true,
                data: {"cerCode":item.cerUuid,"cerType":item.cerType},
                dataType: "json",
                success: function(data) {
                    if (data.res == 1) {
                        UcsmyIndex.alert("提示", data.des);
                        me.refs.grid.reload();
                    } else {
                        UcsmyIndex.alert("错误", data.des);
                    }
                },
                error: function(xhr, errorText, errorType){
                    UcsmyIndex.alert("错误", "网络异常，请稍后再试");
                }
            })
        })
    },
    _revoke: function(item) {
        var me = this;
        UcsmyIndex.confirm("确认","确定吊销该证书？",function(){
            $.ajax({
                url : "cert/revoke",
                type:'post',
                async:true,
                data: {"cerCode":item.cerUuid,"cerType":item.cerType},
                dataType: "json",
                success:function(data){
                    if (data.res == 1){
                        UcsmyIndex.alert("提示", data.des);
                        me.refs.grid.reload();
                    } else{
                        UcsmyIndex.alert("错误", data.des);
                    }
                },
                error:function(xhr, errorText, errorType){
                    UcsmyIndex.alert("错误", "网络异常，请稍后再试");
                }
            });
        });
    },
    render: function() {
        var me = this;
        return (
            <div>
                <h2 className="content-title">{me.props.name}</h2>
                <div className="panel">
                    <div className="panel-title">查询条件</div>
                    <div className="panel-content">
                        <FormItem label="域名"><Input ref="domainName"/></FormItem>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button buttonType="bidnow" onClick={this._query}>查询</Button>
                    <Button buttonType="bidnow" onClick={this._add}>生成证书</Button>
                    <Button buttonType="bidnow" onClick={this._query}>批量生成证书</Button>
                </div>
                <div className="table-panel">
                    <Grid
                        pageSize="5"
                        retDataProperty="data.resultList"
                        retTotalProperty="data.totalCount"
                        retCurrentProperty="data.pageNo"
                        url={"cert/list/" + me.props.type} ref = "grid"
                        columns={[
                        {
                            name: 'domainName', header: '域名'
                        },
                        {
                            name: 'city', header: '城市'
                        },
                        {
                            name: 'orgUnitName', header: '组织单位名'
                        },
                        {
                            name: 'machineCode', header: '机器码'
                        },
                        {
                            name: 'cerStatus', header: '证书状态', content:function(column){
                                return (<span>
                                {column.cerStatus == '1' ? '有效' : '无效'}
                            </span>)
                            }
                        },
                        {
                            name: 'passcode', header: '通行码', content:function(column){
                                    var array = new Array();
                                    var list = column.fileList;
                                    for (var i = 0; i < list.length; i++) {
                                        if(list[i].p12Secret != null) {
                                            array.push(list[i].p12Secret);
                                        }
                                    }
                                    var pc = array.join(",");

                                return (<span>{pc}</span>)
                            }
                        },
                        {
                            name: 'cz',
                                header: '操作',
                                content:function(column){
                                return (<span>
                                    <a href="Javascript:void(0);" onClick={me._download.bind(this, column.fileList)}>下载</a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="Javascript:void(0);" onClick={me._revoke.bind(this, column)}>吊销</a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="Javascript:void(0);" onClick={me._delete.bind(this, column)}>删除</a>
                            </span>)
                            }
                        }
                        ]}
                    />
                    <div className="clearfix"></div>
                    <DownloadPanel ref="download" />
                </div>
            </div>
        );
    }
});