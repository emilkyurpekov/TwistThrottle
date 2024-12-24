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
        imageWidths = images.map((img) => img.offsetWidth + 10);
        totalWidth = imageWidths.reduce((acc, width) => acc + width, 0);
    }

    function moveImages() {
        currentTranslateX -= config.step;

        const firstImageWidth = imageWidths[0];
        if (Math.abs(currentTranslateX) >= firstImageWidth) {
            const firstImage = images.shift();
            currentTranslateX += firstImageWidth;

            imageList.style.transition = "none";
            imageList.appendChild(firstImage);
            images.push(firstImage);

            imageList.style.transform = `translateX(${currentTranslateX}px)`;
            requestAnimationFrame(() => {
                imageList.style.transition = "transform 0.1s linear";
            });

            calculateImageWidths();
        }

        imageList.style.transform = `translateX(${currentTranslateX}px)`;

        requestAnimationFrame(moveImages);
    }

    function handleResize() {
        calculateImageWidths();
    }

    calculateImageWidths();
    requestAnimationFrame(moveImages);

    window.addEventListener("resize", handleResize);
});
