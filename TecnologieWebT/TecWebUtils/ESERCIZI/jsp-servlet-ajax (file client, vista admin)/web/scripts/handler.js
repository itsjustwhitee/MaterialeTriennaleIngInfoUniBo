function filesRequest()
{
    let file1 = document.getElementById('file1').value;
    let file2 = document.getElementById('file2').value;
	
	if(file1 && file2)
	{
			
			let totUpper1 = 0;
			let totLower1 = 0;
			let totUpper2 = 0;
			let totLower2 = 0;
			let received = 0;
			let data = { file1: '', file2: ''};
			let chunk1 = Math.round(file1.length / 3);
			let chunk2 = Math.round(file2.length / 3);
			console.log('Generati i chunk per file1=', chunk1, ' per file2=', chunk2);
			
			for(let i = 1; i < 4; i++)
			{
				if(i < 3)
				{	
					data.file1 = file1.substring((i-1)*chunk1, (i*chunk1));
					data.file2 = file2.substring((i-1)*chunk2, (i*chunk2));
				}
				else
				{
					data.file1 = file1.substring((i-1)*chunk1);
					data.file2 = file2.substring((i-1)*chunk2);
				}
				console.log('Mando porzione ', i, '\n\nfile1:\n', data.file1,'\n\nfile2:\n', data.file2);
				fetch('Counter?part=' + i, {
					method:'POST',
					headers: {'Content-Type': 'application/json'},
					body: JSON.stringify(data)
				})
				.then(response => {
					console.log('Risposta ', i, response);
					return response.text();
				})
				.then(rawText => {
					return JSON.parse(rawText);
				})
				.then(result => {
					totUpper1 += result.totUpper1;
					totUpper2 += result.totUpper2;
					totLower1 += result.totLower1;
					totLower2 += result.totLower2;
					received++;
					
					if(received == 3)
					{
						document.getElementById('response').innerHTML = `Lettura terminata: 
																		<ul>
																			<li>File 1: ` +totLower1 + ` minuscoli e `
																			+ totUpper1 + ` maiuscoli </li>
																			<li>File 2: ` + totLower2 + ` minuscoli e `
																			+ totUpper2 + ` maiuscoli </li>
																		</ul>`
					}
				})
				.catch(error => {
					console.error('Errore: ', error);
				})
			}
			
		alert('Conteggio terminato');
	}
	else
		alert('Uno o piú file non caricati!');    
    return false;
}