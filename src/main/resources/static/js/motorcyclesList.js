function toggleMotorcycles() {
    const button = document.getElementById('showMotorcyclesButton');
    const motorcycleDisplayArea = document.getElementById('motorcycleDisplayArea');

    if (motorcycleDisplayArea.style.display === 'block') {
        // If motorcycles are displayed, hide them
        motorcycleDisplayArea.style.display = 'none';
        button.textContent = 'Show Motorcycles'; // Change button text to "Show Motorcycles"
    } else {
        // If motorcycles are hidden, fetch and show them
        button.disabled = true; // Disable button to prevent multiple clicks

        fetch('/motorcycles')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Fetched motorcycles:', data);

                const motorcycleList = document.getElementById('motorcycles');
                motorcycleList.innerHTML = ''; // Clear existing list

                if (Array.isArray(data) && data.length > 0) {
                    data.forEach(motorcycle => {
                        const listItem = document.createElement('li');
                        listItem.textContent = `${motorcycle.make} ${motorcycle.model} - ${motorcycle.year}`;
                        motorcycleList.appendChild(listItem);
                    });
                } else {
                    const emptyMessage = document.createElement('li');
                    emptyMessage.textContent = 'No motorcycles found for this user.';
                    motorcycleList.appendChild(emptyMessage);
                }

                motorcycleDisplayArea.style.display = 'block'; // Show the list
                button.textContent = 'Hide Motorcycles'; // Change button text to "Hide Motorcycles"
            })
            .catch(error => {
                console.error('Error fetching motorcycles:', error);
                alert('Failed to load motorcycles. Please try again later.');
            })
            .finally(() => {
                button.disabled = false; // Re-enable button after fetch completes
            });
    }
}
