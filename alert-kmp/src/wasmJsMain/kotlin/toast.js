function showToast(toastId, duration) {
    var toast = document.getElementById(toastId);
    if (!toast) return;

    setTimeout(function() {
        toast.style.opacity = '1';
    }, 100);

    setTimeout(function() {
        toast.style.opacity = '0';
        setTimeout(function() {
            if (toast.parentElement) {
                toast.parentElement.removeChild(toast);
            }
        }, 500);
    }, duration);
}
