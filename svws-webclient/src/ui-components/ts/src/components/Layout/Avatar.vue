<script setup lang='ts'>
import imageFile from "../../assets/img/avatar_placeholder.svg";

const {
	src = '',
	alt = '',
} = defineProps<{
	src?: string;
	alt?: string;
}>();

function onError(e: Event) {
	(e.target as HTMLImageElement).src = imageFile;
}
</script>

<template>
	<div class="avatar">
		<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
			<path
				d="M20 22h-2v-2a3 3 0 0 0-3-3H9a3 3 0 0 0-3 3v2H4v-2a5 5 0 0 1 5-5h6a5 5 0 0 1 5 5v2zm-8-9a6 6 0 1 1 0-12 6 6 0 0 1 0 12zm0-2a4 4 0 1 0 0-8 4 4 0 0 0 0 8z"
				/>
		</svg>
		<img :src="src" :alt="alt" @error="onError"/>
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

	img {
		@apply w-full h-full absolute top-0 left-0 object-cover object-center z-10;
	}
}

.avatar--placeholder {
	@apply bg-error;
}
</style>
