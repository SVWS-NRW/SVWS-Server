<template>
	<div class="svws-ui-avatar" :class="{'is-capturing': isCapturing}">
		<div v-if="capture || upload || (src.length > 0)" tabindex="0" class="avatar--edit">
			<span class="avatar--edit-trigger">
				<span class="icon i-ri-camera-line w-full h-full opacity-50 inline-block" />
			</span>
			<svws-ui-button v-if="src && (src.split(',').length > 1)" type="icon" @click="deleteImage" tabindex="0" title="Bild löschen">
				<span class="icon i-ri-delete-bin-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="upload && (uploadedImage === null) && (src.split(',').length < 2)" type="icon" @click="toggleUpload" tabindex="0" title="Bild hochladen">
				<input class="hidden" ref="fileInputEl" type="file" accept="image/*" @change="onFileChanged">
				<span class="icon i-ri-upload-2-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="capture && (currentSnapshot === null)" type="icon" @click="toggleCapturing" tabindex="0" title="Aufnahme starten">
				<span class="icon i-ri-camera-fill inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="capture && isCapturing && (currentSnapshot !== null)" type="icon" @click="toggleSnapshot" tabindex="0" title="Aufnahme löschen">
				<span class="icon i-ri-delete-bin-line inline-block" />
			</svws-ui-button>
		</div>
		<div class="avatar--capture-control" v-if="isCapturing">
			<svws-ui-button v-if="currentSnapshot !== null" type="icon" @click="toggleSnapshot" tabindex="0" title="Neue Aufnahme">
				<span class="icon i-ri-refresh-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot === null" type="icon" @click="stopCapturing" tabindex="0" title="Aufnahme abbrechen">
				<span class="icon i-ri-camera-off-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot === null" type="primary" @click="toggleSnapshot" tabindex="0" title="Bild aufnehmen">
				<span class="icon i-ri-fullscreen-line inline-block" />
				<span>Bild aufnehmen</span>
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot" type="primary" @click="toggleCapturing" tabindex="0" title="Bild speichern">
				<span>Bild speichern</span>
				<span class="icon i-ri-check-line inline-block" />
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
	import { ref, computed, onUnmounted } from "vue";
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

	const fileInputEl = ref<null | HTMLInputElement>(null);
	const uploadedImage = ref<string | null>(null);
	const file = ref<File | null>(null);

	function deleteImage() {
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
			uploadedImage.value = e.target?.result?.toString() ?? null;
			emit('image:base64', uploadedImage.value?.split(',').pop() ?? null);
		};
		reader.readAsDataURL(file.value);
	}

	const isCapturing = ref(false);
	const capturedImage = ref<Blob | null>(null);
	const capturingError = ref("");
	const videoEl = ref<HTMLVideoElement>();
	const canvasEl = ref<HTMLCanvasElement>();
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
			capturingError.value = (err as { message: string }).message;
		}
	}

	function stopCapturing() {
		stream.value?.getTracks().forEach(track => track.stop());
		capturedImage.value = null;
		isCapturing.value = false;
	}

	function takeSnapshot() {
		if ((canvasEl.value === undefined) || (videoEl.value === undefined))
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

<style lang="postcss">
.svws-ui-avatar {
	@apply relative;

	.avatar--edit-trigger {
		@apply bg-ui border border-ui-neutral;
		@apply w-6 h-6 p-0.5 rounded mt-auto ml-auto -mr-1.5 -mb-0.5;
		/* TODO: COLORS icon */
	}
}

.avatar {
	@apply bg-ui-neutral border border-ui-secondary;
	@apply w-full rounded-xl overflow-hidden relative;
	padding-bottom: 100%;

	&--has-image {
		@apply border-transparent;
	}

	svg {
		@apply text-ui-secondary opacity-50;
		@apply absolute -bottom-0.5 w-full h-5/6;
		margin-bottom: -5%;
	}

	img,
	video {
		@apply w-full h-full absolute top-0 left-0 object-cover object-center z-10;
	}

	video {
		@apply bg-ui-neutral;
	}

	&--edit {
		@apply absolute top-0 left-0 z-20 w-full h-full;
		@apply flex items-center justify-center;

		.is-capturing & {
			@apply hidden;
		}

		.button {
			@apply hidden;
		}

		.button--icon {
			padding: 0.4rem;
		}

		&:hover,
		&:focus,
		&:focus-within {
			@apply outline-none bg-ui rounded-xl border border-ui-disabled;

			.avatar--edit-trigger {
				@apply hidden;
			}

			.button {
				@apply block;
			}
		}

		&:focus-visible {
			+ .avatar {
				@apply ring ring-ui-brand;
			}
		}
	}

	.is-capturing & {
		@apply opacity-100;
	}

	&--capture-control {
		@apply flex items-center justify-center gap-1 mt-4;
	}
}

.is-capturing {
	@apply bg-ui;
	@apply fixed z-50 h-screen;
	@apply flex flex-col justify-center items-center;

	.avatar {
		@apply p-0 -order-1 border-none;
		@apply rounded-3xl;
		width: 90vmin;
		height: 90vmin;
		@apply md:w-[37.5rem] md:h-[37.5rem];
	}
}
</style>
