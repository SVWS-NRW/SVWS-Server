<template>
	<div class="page--content">
		<s-card-schueler-kaoa :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';

	const props = defineProps<SchuelerKAoAProps>();

</script>
