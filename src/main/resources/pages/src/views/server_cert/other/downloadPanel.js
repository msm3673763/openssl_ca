var Layer = UcsmyUI.Layer;
var Checkbox = UcsmyUI.Checkbox;

module.exports = React.createClass({
    getInitialState: function() {
        return {
            fileDiv : []
        }
    },
    componentDidMount: function() {

    },
    init: function(data) {
        var fileDiv = [];
        if(data){
            for(i = 0; i < data.length; i++){
                var file = data[i];
                fileDiv.push(<div className="downloadDiv">
                            <Checkbox name="fileCheck" value={file.fileDBId} />
                                <span className="text">.{file.fileType}</span>
                            </div>);
            }
        }
        this.setState({
            fileDiv : fileDiv
        });
        this.refs.layer.layerOpen();
    },
    _close: function() {
        this.refs.layer.layerClose();
    },
    _download: function() {
        var me = this;
        var obj = document.getElementsByName("fileCheck");
        var checkObj = new Array();

        for(var k=0; k < obj.length; k++) {
            if(obj[k].checked){
                var file = obj[k].getAttribute("value");
                checkObj.push(file);
            }
        }

        if (checkObj.length == 0 ){
            UcsmyIndex.alert("异常消息", "至少必须选择一个文件");
            return;
        }
        window.location.href = "cert/ca/file/" + checkObj.join(",");

    },
    render: function() {
        var me = this;
        return (
            <div className="downloadLayer">
                <Layer title=" " width="315" height="250" ref="layer" confirm="下载" cancel="取消" confirmBack={me._download}>
                    <div className="fileDiv">
                        {me.state.fileDiv}
                    </div>
                </Layer>
            </div>
        );
    }
});