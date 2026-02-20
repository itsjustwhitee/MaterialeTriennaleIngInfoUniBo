'use strict';

class App extends React.Component {
  constructor(){
        super();

	//valori iniziali di state
    this.state = {
        
    };

	//bind funzioni
    this.loadGame = this.load.bind(this);
    this.onClick = this.onClick.bind(this);
	
    console.log('Settato state iniziale: ', this.state);
  }

  load(event)
  {
    event.preventDefault();
    
  }

  onClick(e) 
  {
    let element = e.target.id; //id bottone 
	
  };

  render() 
  {
      return (
		<>
			
        </>);
  }
}