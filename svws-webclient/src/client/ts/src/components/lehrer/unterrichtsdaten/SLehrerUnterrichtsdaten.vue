<template>
	<div class="page--content page--content--flex">
		<div class="svws-ui-empty">
			<i-ri-stack-line />
			<span>Hier werden zukünftig<br>die Unterrichtsdaten angezeigt.</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { LehrerUnterrichtsdatenProps } from './SLehrerUnterrichtsdatenProps';

	const props = defineProps<LehrerUnterrichtsdatenProps>();

</script>
