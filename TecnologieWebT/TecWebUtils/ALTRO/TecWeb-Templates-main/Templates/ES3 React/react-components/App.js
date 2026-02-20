'use strict';

class App extends React.Component {
    constructor(){
        super();
        this.state = {
            value: ""
            }
            //fai le bind
            this.start = this.start.bind(this);
    }

    start(){
        console.log("Reset");
        this.setState({
         value: "started"
        });
    }

    render() {
        return (
            <div >
               <button type="button" value="Inserisci" onClick={this.start}>Inizio</button>
               <br/>
                {this.state.value}
                <br/>
                <Pista

                />
               <br/>
                <Classifica

                />

            </div>
        );
    }
}
