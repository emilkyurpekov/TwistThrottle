document.addEventListener("DOMContentLoaded", function () {
    const config = {
        step: 2, // Speed of movement (increase safely)
        resizeDebounceTime: 2100, // Debounce time for resize
    };

    const imageList = document.getElementById("imageList");
    const images = Array.from(imageList.children);

    let imageWidths = []; // Store the width of each image
    let totalWidth = 0; // Total width of all images
    let currentTranslateX = 0; // Current translation of the list

    function calculateImageWidths() {
        // Recalculate each image's width and the total width
        imageWidths = images.map((img) => img.offsetWidth + 10); // Include margin-right
        totalWidth = imageWidths.reduce((acc, width) => acc + width, 0);
    }

    function moveImages() {
        currentTranslateX -= config.step;

        const firstImageWidth = imageWidths[0];
        if (Math.abs(currentTranslateX) >= firstImageWidth) {
            const firstImage = images.shift();
            currentTranslateX += firstImageWidth;

            imageList.style.transition = "none"; // Disable transition temporarily
            imageList.appendChild(firstImage); // Move the first image to the end
            images.push(firstImage); // Update the image array

            // Immediately reposition the image list to maintain smooth scrolling
            imageList.style.transform = `translateX(${currentTranslateX}px)`;
            requestAnimationFrame(() => {
                imageList.style.transition = "transform 0.1s linear"; // Re-enable animation
            });

            // Recalculate widths in case of dynamic updates
            calculateImageWidths();
        }

        // Apply the translation for smooth animation
        imageList.style.transform = `translateX(${currentTranslateX}px)`;

        // Continue the animation
        requestAnimationFrame(moveImages);
    }

    function handleResize() {
        calculateImageWidths(); // Recalculate dimensions on resize
    }

    // Initial setup
    calculateImageWidths();
    requestAnimationFrame(moveImages);

    window.addEventListener("resize", handleResize);
});
