<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-wenom />
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Datenaustausch mit dem Web-Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	import type { SchuleDatenaustauschWenomProps } from './SSchuleDatenaustauschWenomProps';
	const props = defineProps<SchuleDatenaustauschWenomProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>
