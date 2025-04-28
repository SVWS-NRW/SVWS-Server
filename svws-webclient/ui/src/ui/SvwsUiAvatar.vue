<template>
	<div class="svws-ui-avatar" :class="{'is-capturing': isCapturing}">
		<div v-if="capture || upload || (src.length > 0)" tabindex="0" class="avatar--edit">
			<span class="avatar--edit-trigger">
				<span class="icon i-ri-camera-line w-full h-full opacity-50" />
			</span>
			<svws-ui-button v-if="src && (src.split(',').length > 1)" type="icon" @click="deleteImage" tabindex="0" title="Bild löschen">
				<span class="icon i-ri-delete-bin-line" :class="{'icon-ui-caution': stage}" />
			</svws-ui-button>
			<svws-ui-button v-if="upload && (uploadedImage === null) && (src.split(',').length < 2)" type="icon" @click="toggleUpload" tabindex="0" title="Bild hochladen">
				<input class="hidden" ref="fileInputEl" type="file" accept="image/*" @change="onFileChanged">
				<span class="icon i-ri-upload-2-line" />
			</svws-ui-button>
			<svws-ui-button v-if="capture && (currentSnapshot === null) && hasVideoDevice" type="icon" @click="toggleCapturing" tabindex="0" title="Aufnahme starten">
				<span class="icon i-ri-camera-fill" />
			</svws-ui-button>
			<svws-ui-button v-if="capture && isCapturing && (currentSnapshot !== null)" type="icon" @click="toggleSnapshot" tabindex="0" title="Aufnahme löschen">
				<span class="icon i-ri-delete-bin-line" />
			</svws-ui-button>
		</div>
		<div class="avatar--capture-control" v-if="isCapturing">
			<svws-ui-button v-if="currentSnapshot !== null" type="icon" @click="toggleSnapshot" tabindex="0" title="Neue Aufnahme">
				<span class="icon i-ri-refresh-line" />
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot === null" type="icon" @click="stopCapturing" tabindex="0" title="Aufnahme abbrechen">
				<span class="icon i-ri-camera-off-line" />
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot === null" type="primary" @click="toggleSnapshot" tabindex="0" title="Bild aufnehmen">
				<span class="icon i-ri-fullscreen-line" />
				<span>Bild aufnehmen</span>
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot" type="primary" @click="toggleCapturing" tabindex="0" title="Bild speichern">
				<span>Bild speichern</span>
				<span class="icon i-ri-check-line" />
			</svws-ui-button>
		</div>
		<div class="avatar" :class="{'avatar--has-image': uploadedImage}">
			<template v-if="isCapturing">
				<span v-if="capturingError">{{ capturingError }}</span>
				<template v-else>
					<video playsinline ref="videoEl" autoplay id="video" />
					<canvas ref="canvasEl" id="canvas" class="hidden" />
					<img v-if="currentSnapshot !== null" :src="currentSnapshot" :alt>
				</template>
			</template>
			<template v-else>
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
					<path d="M20 22h-2v-2a3 3 0 0 0-3-3H9a3 3 0 0 0-3 3v2H4v-2a5 5 0 0 1 5-5h6a5 5 0 0 1 5 5v2zm-8-9a6 6 0 1 1 0-12 6 6 0 0 1 0 12zm0-2a4 4 0 1 0 0-8 4 4 0 0 0 0 8z" />
				</svg>
				<img :src :alt @error="onError">
			</template>
		</div>
	</div>
</template>

<script setup lang='ts'>
	import { ref, computed, onUnmounted, onMounted } from "vue";
	import imageFile from "../assets/img/avatar_placeholder.svg";

	const props = withDefaults(defineProps<{
		src?: string;
		alt?: string;
		upload?: boolean;
		capture?: boolean;
	}>(), {
		src: '',
		alt: '',
		upload: false,
		capture: false,
	});

	const emit = defineEmits<{
		'image:captured': [value: Blob | null];
		'image:base64': [val: string | null];
	}>();

	const hasVideoDevice = ref(false);

	const stage = ref<boolean>(false);

	onMounted(async () => {
		// test if media device available
		const list = await navigator.mediaDevices.enumerateDevices();
		for (const device of list)
			if (device.kind === 'videoinput')
				hasVideoDevice.value = true;
	})

	const fileInputEl = ref<null | HTMLInputElement>(null);
	const uploadedImage = ref<string | null>(null);
	const file = ref<File | null>(null);

	function deleteImage() {
		stage.value = !stage.value;
		if (stage.value)
			return;
		emit('image:base64', null);
		emit('image:captured', null);
	}

	function toggleUpload() {
		if (uploadedImage.value !== null)
			uploadedImage.value = null;
		else
			fileInputEl.value?.click()
	}

	function toggleSnapshot() {
		if (capturedImage.value)
			capturedImage.value = null;
		else
			takeSnapshot();
	}

	async function toggleCapturing() {
		if (isCapturing.value) {
			emit('image:captured', capturedImage.value);
			emit('image:base64', canvasEl.value?.toDataURL('image/jpeg', 0.75).split(',').pop() ?? null);
			stopCapturing();
		}
		else
			await startCapturing();
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files === null)
			return;
		file.value = target.files[0];
		emit('image:captured', file.value);
		const reader = new FileReader();
		reader.onload = e => {
			if (typeof e.target?.result === 'string') {
				uploadedImage.value = e.target.result;
				emit('image:base64', e.target.result.split(',').pop() ?? null);
			} else {
				uploadedImage.value = null;
				emit('image:base64', null);
			}
		};
		reader.readAsDataURL(file.value);
	}

	const isCapturing = ref(false);
	const capturedImage = ref<Blob | null>(null);
	const capturingError = ref("");
	const videoEl = ref<HTMLVideoElement>();
	const canvasEl = ref<HTMLCanvasElement | null>(null);
	const stream = ref<MediaStream>();

	const constraints = {
		video: { width: 320 },
		facingMode: 'user',
	};

	const currentSnapshot = computed(() => {
		if (capturedImage.value !== null)
			return URL.createObjectURL(capturedImage.value);
		return null;
	});

	async function startCapturing() {
		capturedImage.value = null;
		isCapturing.value = true;
		try {
			stream.value = await navigator.mediaDevices.getUserMedia(constraints);
			if (videoEl.value)
				videoEl.value.srcObject = stream.value;
		} catch (err) {
			console.log(err)
			capturingError.value = (err as { message: string }).message;
		}
	}

	function stopCapturing() {
		stream.value?.getTracks().forEach(track => track.stop());
		capturedImage.value = null;
		isCapturing.value = false;
	}

	function takeSnapshot() {
		if ((videoEl.value === undefined) || (canvasEl.value === null))
			return;
		canvasEl.value.width = constraints.video.width;
		canvasEl.value.height = videoEl.value.videoHeight / (videoEl.value.videoWidth / constraints.video.width);
		canvasEl.value.getContext("2d")?.drawImage(videoEl.value, 0, 0);
		canvasEl.value.toBlob((blob: Blob | null) => capturedImage.value = blob);
	}

	onUnmounted(() => stopCapturing());

	function onError(e: Event) {
		(e.target as HTMLImageElement).src = imageFile;
	}

</script>
