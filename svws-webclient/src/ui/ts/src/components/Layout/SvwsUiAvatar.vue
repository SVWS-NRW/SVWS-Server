<script setup lang='ts'>
	import useCapturing from "../../composables/use-capturing";
	import useUploading from "../../composables/use-uploading";
	import imageFile from "../../assets/img/avatar_placeholder.svg";

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

	defineEmits<{
		(e: 'image:captured', val: Blob | null): void;
	}>();

	const {
		videoEl,
		canvasEl,
		isCapturing,
		currentSnapshot,
		toggleCapturing,
		capturingError,
		toggleSnapshot
	} = useCapturing();
	const {fileInputEl, toggleUpload, onSelectFile, uploadedImage} = useUploading();

	function onError(e: Event) {
		(e.target as HTMLImageElement).src = imageFile;
	}
</script>

<template>
	<div class="relative" :class="{'is-capturing': isCapturing}">
		<div class="avatar--edit" v-if="capture || upload || src" tabindex="0">
			<span class="avatar--edit-trigger w-6 h-6 p-0.5 rounded bg-light dark:bg-white/10 mt-auto ml-auto -mr-1.5 -mb-0.5 border border-black/10 dark:border-white/10">
				<i-ri-camera-line class="w-full h-full opacity-50" />
			</span>

			<svws-ui-button type="icon" @click="toggleUpload" v-if="src && src.split('data:image/png;base64, /').length > 1" tabindex="0"
				title="Bild löschen">
				<i-ri-delete-bin-line />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleUpload" v-if="upload && !uploadedImage && src.split('data:image/png;base64, /').length < 2" tabindex="0"
				title="Bild hochladen">
				<input class="hidden" ref="fileInputEl" type="file" accept="image/*" @change="onSelectFile">
				<i-ri-upload-2-line />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleUpload" v-if="upload && uploadedImage" tabindex="0"
				title="Bild löschen">
				<i-ri-delete-bin-line />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleCapturing" v-if="capture && !currentSnapshot"
				tabindex="0" title="Aufnahme starten">
				<i-ri-camera-fill />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleSnapshot"
				v-if="capture && isCapturing && currentSnapshot" tabindex="0" title="Aufnahme löschen">
				<i-ri-delete-bin-line />
			</svws-ui-button>
		</div>
		<div class="avatar--capture-control" v-if="isCapturing">
			<svws-ui-button v-if="currentSnapshot" type="icon" @click="toggleSnapshot" tabindex="0" title="Neue Aufnahme">
				<i-ri-refresh-line />
			</svws-ui-button>
			<svws-ui-button v-if="!currentSnapshot" type="icon" @click="toggleCapturing" tabindex="0" title="Aufnahme abbrechen">
				<i-ri-camera-off-line />
			</svws-ui-button>
			<svws-ui-button v-if="!currentSnapshot" type="primary" @click="toggleSnapshot" tabindex="0" title="Bild aufnehmen">
				<i-ri-fullscreen-line />
				<span>Bild aufnehmen</span>
			</svws-ui-button>
			<svws-ui-button v-if="currentSnapshot" type="primary" @click="toggleCapturing" tabindex="0" title="Bild speichern">
				<span>Bild speichern</span>
				<i-ri-check-line />
			</svws-ui-button>
		</div>
		<div class="avatar">
			<template v-if="isCapturing">
				<span v-if="capturingError">{{ capturingError }}</span>
				<template v-else>
					<video playsinline ref="videoEl" autoplay id="video" />
					<canvas ref="canvasEl" id="canvas" class="hidden" />
					<img :src="currentSnapshot" :alt="alt" v-if="currentSnapshot">
				</template>
			</template>
			<template v-else-if="uploadedImage">
				<img :src="uploadedImage" :alt="alt">
			</template>
			<template v-else>
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
					<path
						d="M20 22h-2v-2a3 3 0 0 0-3-3H9a3 3 0 0 0-3 3v2H4v-2a5 5 0 0 1 5-5h6a5 5 0 0 1 5 5v2zm-8-9a6 6 0 1 1 0-12 6 6 0 0 1 0 12zm0-2a4 4 0 1 0 0-8 4 4 0 0 0 0 8z" />
				</svg>
				<img :src="currentSnapshot" :alt="alt" v-if="currentSnapshot">
				<img :src="src" :alt="alt" @error="onError">
			</template>
		</div>
	</div>
</template>

<style lang="postcss">
.avatar {
	@apply w-full rounded-lg overflow-hidden relative bg-light;
	@apply border border-black/5 dark:border-white/5;
	padding-bottom: 100%;

	svg {
		@apply absolute -bottom-0.5 w-full h-5/6 text-svws-950 opacity-20;
		margin-bottom: -5%;
	}

	img,
	video {
		@apply w-full h-full absolute top-0 left-0 object-cover object-center z-10;
	}

	video {
		@apply bg-light;
	}

	&--edit {
		@apply absolute top-0 left-0 z-20 w-full h-full;
		@apply flex items-center justify-center;

		.is-capturing & {
			@apply hidden;
		}

		.button {
			@apply hidden;

			&:hover, &:focus {
				@apply bg-opacity-100;
			}
		}

		.button--icon {
			padding: 0.4rem;
		}

		&:hover,
		&:focus,
		&:focus-within {
			@apply outline-none;

			.avatar--edit-trigger {
				@apply hidden;
			}

			.button {
				@apply block;
			}

			+ .avatar {
				@apply opacity-10 border-black/50;
			}
		}

		&:focus-visible {
			+ .avatar {
				@apply ring ring-primary;
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
	@apply fixed inset-0 z-[100] h-screen;
	@apply bg-light/90 dark:bg-[#000]/90 backdrop-filter backdrop-grayscale;
	@apply flex flex-col justify-center items-center;

	.avatar {
		@apply p-0 -order-1;
		@apply rounded-3xl;
		width: 90vmin;
		height: 90vmin;
		@apply md:w-[37.5rem] md:h-[37.5rem];
	}
}
</style>
