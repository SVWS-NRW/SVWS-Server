export default function useCapturing() {
	const vm = getCurrentInstance();
	// @ts-expect-error mis-alignment with @vue/composition-api
	const emit = vm?.emit || vm?.$emit?.bind(vm) || vm?.proxy?.$emit?.bind(vm?.proxy);

	const isCapturing = ref(false);
	const capturedImage = ref<Blob | null>();
	const capturingError = ref("");
	const videoEl = ref<HTMLVideoElement>();
	const canvasEl = ref<HTMLCanvasElement>();
	const stream = ref<MediaStream>();

	const constraints = {
		video: {
			width: 1280,
			height: 720
		},
		facingMode: 'user',
		audio: false
	};

	const currentSnapshot = computed(() => {
		if (capturedImage.value) {
			return URL.createObjectURL(capturedImage.value);
		}

		return null;
	});

	async function startCapturing() {
		isCapturing.value = true;

		if (navigator.mediaDevices) {
			try {
				stream.value = await navigator.mediaDevices.getUserMedia(constraints);

				if (videoEl.value) {
					videoEl.value.srcObject = stream.value;
				}
			} catch (err) {
				capturingError.value = (err as { message: string }).message;
			}
		} else {
			capturingError.value = "Ihr Browser unterstützt diese Funktion leider nicht.";
		}
	}

	function stopCapturing() {
		stream.value?.getTracks().forEach(track => track.stop());
		capturedImage.value = null;
		isCapturing.value = false;
	}

	function takeSnapshot() {
		const { width, height } = constraints.video;
		if (canvasEl.value && videoEl.value) {
			canvasEl.value.width = width;
			canvasEl.value.height = height;

			canvasEl.value.getContext("2d")?.drawImage(videoEl.value, 0, 0);
			canvasEl.value?.toBlob((blob: Blob | null) => {
				capturedImage.value = blob;
				emit("image:captured", blob);
			});
		}
	}

	function toggleSnapshot() {
		if (capturedImage.value) {
			capturedImage.value = null;
		} else {
			takeSnapshot();
		}
	}

	function toggleCapturing() {
		if (isCapturing.value) {
			stopCapturing();
		} else {
			startCapturing();
		}
	}

	onUnmounted(() => {
		stopCapturing();
	})

	return {
		videoEl,
		canvasEl,
		isCapturing,
		toggleCapturing,
		currentSnapshot,
		toggleSnapshot,
		capturingError
	};
}
