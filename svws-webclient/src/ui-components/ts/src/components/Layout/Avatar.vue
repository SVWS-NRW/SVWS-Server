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
	<img :src="src" :alt="alt" class="avatar" @error="onError" />
</template>

<style>
.avatar {
	@apply h-32 w-32 rounded-full object-cover;
}
</style>
