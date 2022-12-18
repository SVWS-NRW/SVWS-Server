<template>
	<div v-if="visible">
		<svws-ui-content-card title="Platzhalter">
			<div class="input-wrapper">
				<p>Hier werden zukünftig die Unterrichtsdaten angezeigt.</p>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerUnterrichtsdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
