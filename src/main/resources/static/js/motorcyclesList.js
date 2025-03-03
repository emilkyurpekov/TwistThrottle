function toggleMotorcycles() {
    const button = document.getElementById('showMotorcyclesButton');
    const motorcycleDisplayArea = document.getElementById('motorcycleDisplayArea');

    if (motorcycleDisplayArea.style.display === 'block') {
        motorcycleDisplayArea.style.display = 'none';
        button.textContent = 'Show Motorcycles';
    } else {
        button.disabled = true;

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
                motorcycleList.innerHTML = '';

                if (Array.isArray(data) && data.length > 0) {
                    data.forEach((motorcycle, index) => {
                        const listItem = document.createElement('li');
                        listItem.className = 'motorcycle-item';
                        listItem.textContent = `${motorcycle.make} ${motorcycle.model} - ${motorcycle.year}`;

                        const dropdown = document.createElement('div');
                        dropdown.className = 'motorcycle-dropdown';
                        dropdown.style.display = 'none';
                        dropdown.innerHTML = `
                            <p><strong>Type:</strong> ${motorcycle.motorcycleType}</p>
                            <p><strong>Horsepower:</strong> ${motorcycle.horsepower}</p>
                            <p><strong>Weight:</strong> ${motorcycle.weight} kg</p>
                            <p><strong>Volume:</strong> ${motorcycle.volume}cc</p>
                        `;

                        listItem.addEventListener('click', () => {
                            const isVisible = dropdown.style.display === 'block';
                            dropdown.style.display = isVisible ? 'none' : 'block';
                        });

                        listItem.appendChild(dropdown);
                        motorcycleList.appendChild(listItem);
                    });
                } else {
                    const emptyMessage = document.createElement('li');
                    emptyMessage.textContent = 'No motorcycles found for this user.';
                    motorcycleList.appendChild(emptyMessage);
                }

                motorcycleDisplayArea.style.display = 'block';
                button.textContent = 'Hide Motorcycles';
            })
            .catch(error => {
                console.error('Error fetching motorcycles:', error);
                alert('Failed to load motorcycles. Please try again later.');
            })
            .finally(() => {
                button.disabled = false;
            });
    }
}
