var Main = require('../server_cert/other/main');

myPanel = React.createClass({
    getInitialState:function () {
        return{
        }
    },
    render: function() {
        return (
            <Main type="2" name="终端证书管理" />
        );
    }
});