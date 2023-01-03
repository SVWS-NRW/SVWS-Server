export default function useCapturing() {
	const vm = getCurrentInstance();
	// @ts-expect-error mis-alignment with @vue/composition-api
	const emit = vm?.emit || vm?.$emit?.bind(vm) || vm?.proxy?.$emit?.bind(vm?.proxy);

	const fileInputEl = ref<null | HTMLInputElement>();
	const uploadedImage = ref<string | null>(null);

	function uploadImage() {
		fileInputEl.value?.click();
	}

	function onSelectFile() {
		const input = fileInputEl.value;
		const files = input?.files;
		if (files && files[0]) {
			const reader = new FileReader();
			reader.onload = e => {
        emit("image:uploaded", e.target?.result);
				uploadedImage.value = e.target?.result as string ?? null;
			};
			reader.readAsDataURL(files[0]);
		}
	}

  function toggleUpload() {
    if (uploadedImage.value) {
      uploadedImage.value = null;
    } else {
      uploadImage();
    }
  }

	return {
		fileInputEl,
		uploadImage,
		onSelectFile,
		uploadedImage,
    toggleUpload
	};
}
