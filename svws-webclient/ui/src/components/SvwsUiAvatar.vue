<template>
	<div class="svws-ui-avatar" :class="{'is-capturing': isCapturing}">
		<div class="avatar--edit" v-if="capture || upload || src" tabindex="0">
			<span class="avatar--edit-trigger">
				<span class="icon i-ri-camera-line w-full h-full opacity-50 inline-block" />
			</span>

			<svws-ui-button type="icon" @click="toggleUpload" v-if="src && src.split('data:image/png;base64, /').length > 1" tabindex="0"
				title="Bild löschen">
				<span class="icon i-ri-delete-bin-line inline-block" />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleUpload" v-if="upload && !uploadedImage && src.split('data:image/png;base64, /').length < 2" tabindex="0" title="Bild hochladen">
				<input class="hidden" ref="fileInputEl" type="file" accept="image/*" @change="onSelectFile">
				<span class="icon i-ri-upload-2-line inline-block" />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleUpload" v-if="upload && uploadedImage" tabindex="0" title="Bild löschen">
				<span class="icon i-ri-delete-bin-line inline-block" />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleCapturing" v-if="capture && !currentSnapshot" tabindex="0" title="Aufnahme starten">
				<span class="icon i-ri-camera-fill inline-block" />
			</svws-ui-button>
			<svws-ui-button type="icon" @click="toggleSnapshot" v-if="capture && isCapturing && currentSnapshot" tabindex="0" title="Aufnahme löschen">
				<span class="icon i-ri-delete-bin-line inline-block" />
			</svws-ui-button>
		</div>
		<div class="avatar--capture-control" v-if="isCapturing">
			<svws-ui-button v-if="currentSnapshot" type="icon" @click="toggleSnapshot" tabindex="0" title="Neue Aufnahme">
				<span class="icon i-ri-refresh-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="!currentSnapshot" type="icon" @click="toggleCapturing" tabindex="0" title="Aufnahme abbrechen">
				<span class="icon i-ri-camera-off-line inline-block" />
			</svws-ui-button>
			<svws-ui-button v-if="!currentSnapshot" type="primary" @click="toggleSnapshot" tabindex="0" title="Bild aufnehmen">
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

<script setup lang='ts'>
	import useCapturing from "../composables/use-capturing";
	import useUploading from "../composables/use-uploading";
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
	@apply fixed inset-0 z-[100] h-screen;
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
