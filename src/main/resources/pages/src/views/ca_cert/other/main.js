var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var FormItem = UcsmyUI.Form.FormItem;
var CertForm = require('./rootCertForm');

module.exports = React.createClass({
    getInitialState: function() {
        return {
            cerUuid : "",
            domainName : "",
            country : "",
            province : "",
            city : "",
            orgName : "",
            orgUnitName : "",
            email : "",
            fileList : []
        }
    },
    componentDidMount: function() {
        this._init();
    },
    _init: function(){
        var me = this;
        $.ajax({
            url : "cert/ca",
            type:'get',
            async:true,
            data : {},
            dataType: "json",
            success:function(data){
                var objData = data;
                if(objData.retcode == 0){
                    me.setState({
                        cerUuid : objData.data.cerUuid,
                        domainName : objData.data.domainName,
                        country : objData.data.country,
                        province : objData.data.province,
                        city : objData.data.city,
                        orgName : objData.data.orgName,
                        orgUnitName : objData.data.orgUnitName,
                        email : objData.data.email,
                        fileList: objData.data.fileList
                    });
                }
            },
            error:function(xhr, errorText, errorType){
                UcsmyIndex.alert("错误", "网络异常，请稍后再试");
            }
        });
    },
    _reInit: function() {
        var me = this;
        UcsmyIndex.openChildrenPage(CertForm, function(refPanel) {
            refPanel.init(function(){
                me._init();
            });
        });
    },
    _download: function(){
        var array = new Array();
        var list = this.state.fileList;
        for(var i = 0; i < list.length; i++) {
            array.push(list[i].fileDBId);
        }
        window.location.href = "cert/ca/file/" + array.join(",");
    },
    render: function() {
        return (
            <div>
                <h2 className="content-title">CA根证书管理</h2>
                <div className="panel">
                    <div className="panel-title fc-red"></div>
                    <div className="panel-content">
                        <input type="hidden" name="cerUuid" value={this.state.cerUuid}/>
                        <FormItem label="域名"><Input name="domainName" value={this.state.domainName} disabled  /></FormItem>
                        <FormItem label="国家"><Input name="country" value={this.state.country} disabled /></FormItem>
                        <FormItem label="省份"><Input name="province" value={this.state.province} disabled /></FormItem>
                        <FormItem label="城市"><Input name="city" value={this.state.city} disabled /></FormItem>
                        <FormItem label="组织名"><Input name="orgName" value={this.state.orgName} disabled /></FormItem>
                        <FormItem label="组织单位名"><Input name="orgUnitName" value={this.state.orgUnitName} disabled /></FormItem>
                        <FormItem label="邮箱"><Input name="email" value={this.state.email} disabled /></FormItem>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button buttonType="bidnow" onClick={this._reInit}>初始化证书</Button>
                    <Button buttonType="bidnow" onClick={this._download}>下载证书</Button>
                </div>
            </div>
        );
    }
});