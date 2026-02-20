'use strict';
class Scoreboard extends React.Component {
    constructor(){
          super();
    }

    render(){
        if(this.props.moves <= 0)
            return (<></>);

        let items = [];
        for(let i = 1; i <= this.props.moves; i++)
        {
            let name = `move${i}`;
            items.push(<li id={name} key={name}></li>);
        }

        return  (<>
                <h3 className="onDark">Scoreboard</h3>
                <br/>
                <ul id="movesList" className="onDark">{items}</ul>
                <br />
                <h7 className="onDark">Totale: <span id="total">-</span></h7>
                </>);
    }
}