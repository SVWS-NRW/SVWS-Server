<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch mit dem Web-Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page--content page--content--full">
				<s-card-schule-datenaustausch-wenom />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenaustauschWenomProps } from './SSchuleDatenaustauschWenomProps';

	const props = defineProps<SchuleDatenaustauschWenomProps>();

</script>
