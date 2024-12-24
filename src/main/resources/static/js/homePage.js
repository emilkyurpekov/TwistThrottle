document.addEventListener("DOMContentLoaded", function () {
    const config = {
        step: 4, // Speed of movement
        resizeDebounceTime: 300, // Debounce time for resize
    };

    const imageList = document.getElementById("imageList");
    const container = imageList.parentElement; // The visible container
    const images = Array.from(imageList.children);

    let imageWidths = []; // Array to store the width of each image
    let totalWidth = 0; // Total width of the original images
    let currentTranslateX = 0; // Current translation position

    function calculateWidths() {
        // Calculate the width of each image and the total width
        imageWidths = images.map((img) => img.offsetWidth + 10); // Include margin-right
        totalWidth = imageWidths.reduce((sum, width) => sum + width, 0);

        // Clone images if needed to fill the container and ensure smooth scrolling
        const containerWidth = container.offsetWidth;
        while (totalWidth < containerWidth * 2) {
            images.forEach((img) => {
                const clone = img.cloneNode(true);
                imageList.appendChild(clone);
                images.push(clone); // Add the cloned image to the array
                imageWidths.push(clone.offsetWidth + 10);
                totalWidth += clone.offsetWidth + 10;
            });
        }

        // Ensure the list is wide enough to scroll continuously
        imageList.style.width = `${totalWidth}px`;
    }

    function moveImages() {
        // Move the list left
        currentTranslateX -= config.step;

        // When scrolled past the first set of images, reset position to maintain continuity
        if (Math.abs(currentTranslateX) >= totalWidth / 2) {
            currentTranslateX = currentTranslateX % (totalWidth / 2);
        }

        // Apply the transformation
        imageList.style.transform = `translateX(${currentTranslateX}px)`;

        // Request the next frame
        requestAnimationFrame(moveImages);
    }

    function handleResize() {
        calculateWidths(); // Recalculate widths on resize
        currentTranslateX = 0; // Reset position to prevent misalignment
    }

    // Initial setup
    calculateWidths();
    requestAnimationFrame(moveImages);

    // Recalculate on window resize
    window.addEventListener("resize", handleResize);
});
