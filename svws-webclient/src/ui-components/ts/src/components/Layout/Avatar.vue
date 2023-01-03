<script setup lang='ts'>
import imageFile from "../../assets/img/avatar_placeholder.svg";

const {
	src = '',
	alt = '',
	upload = false,
	capture = false,
} = defineProps<{
	src?: string;
	alt?: string;
	upload?: boolean;
	capture?: boolean;
}>();

defineEmits<{
	(e: 'image:captured', val: Blob | null): void;
}>();

const { videoEl, canvasEl, isCapturing, currentSnapshot, toggleCapturing, capturingError, toggleSnapshot } = useCapturing();
const { fileInputEl, toggleUpload, onSelectFile, uploadedImage } = useUploading();

function onError(e: Event) {
	(e.target as HTMLImageElement).src = imageFile;
}
</script>

<template>
	<div>
		<div class="avatar">
			<template v-if="isCapturing">
				<span v-if="capturingError">{{ capturingError }}</span>
				<template v-else>
					<video playsinline ref="videoEl" autoplay id="video"></video>
					<canvas ref="canvasEl" id="canvas" class="hidden"></canvas>
					<img :src="currentSnapshot" :alt="alt" v-if="currentSnapshot" />
				</template>
			</template>
			<template v-else-if="uploadedImage">
				<img :src="uploadedImage" :alt="alt" />
			</template>
			<template v-else>
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
					<path
						d="M20 22h-2v-2a3 3 0 0 0-3-3H9a3 3 0 0 0-3 3v2H4v-2a5 5 0 0 1 5-5h6a5 5 0 0 1 5 5v2zm-8-9a6 6 0 1 1 0-12 6 6 0 0 1 0 12zm0-2a4 4 0 1 0 0-8 4 4 0 0 0 0 8z" />
				</svg>
				<img :src="src" :alt="alt" @error="onError" />
			</template>
		</div>
		<div class="mt-1 flex gap-1" v-if="capture">
			<Button @click="toggleCapturing">
				<span v-if="isCapturing">Aufnahme stoppen</span>
				<span v-else>Aufnahme starten</span>
			</Button>
			<Button v-if="isCapturing" type="secondary" @click="toggleSnapshot">
				<span v-if="!currentSnapshot">Bild aufnehmen</span>
				<span v-else>Bild löschen</span>
			</Button>
		</div>
		<div class="mt-1" v-if="upload">
			<Button @click="toggleUpload">
				<input class="hidden" ref="fileInputEl" type="file" accept="image/*" @change="onSelectFile">
				<span v-if="!uploadedImage">Bild hochladen</span>
				<span v-else>Bild löschen</span>
			</Button>
		</div>
	</div>
</template>

<style lang="postcss">
.avatar {
	@apply w-full rounded-full overflow-hidden relative bg-dark-20 bg-opacity-50;
	padding-bottom: 100%;

	svg {
		@apply absolute bottom-0 w-full h-4/5 text-dark-80 opacity-20;
		margin-bottom: -5%;
	}

	img,
	video {
		@apply w-full h-full absolute top-0 left-0 object-cover object-center z-10;
	}
}

.avatar--placeholder {
	@apply bg-error;
}
</style>
