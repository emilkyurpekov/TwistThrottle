function showMotorcycles() {
    // Fetch motorcycles for the current user
    fetch('/motorcycles')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Fetched motorcycles:', data); // Log the full data for debugging

            // Get the motorcycles container
            const motorcycleList = document.getElementById('motorcycles');
            motorcycleList.innerHTML = ''; // Clear previous content

            // Check if data is valid
            if (Array.isArray(data) && data.length > 0) {
                // Create a list item for each motorcycle
                data.forEach((motorcycle, index) => {
                    const listItem = document.createElement('li');
                    listItem.textContent = `${motorcycle.make} ${motorcycle.model} - ${motorcycle.year}`;
                    motorcycleList.appendChild(listItem);
                });
            } else {
                // Show a message if no motorcycles are found
                const emptyMessage = document.createElement('li');
                emptyMessage.textContent = 'No motorcycles found for this user.';
                motorcycleList.appendChild(emptyMessage);
            }

            // Make the motorcycle display area visible
            document.getElementById('motorcycleDisplayArea').style.display = 'block';
        })
        .catch(error => {
            console.error('Error fetching motorcycles:', error);
            alert('Failed to load motorcycles. Please try again later.');
        });
}
