<template>
	<div class="page--content">
		<svws-ui-todo title="KAoA">
			Dieser Bereich ist noch in Entwicklung. Hier werden die KAoA-Daten abgelegt.
		</svws-ui-todo>
		{{ data() }}
		<br>
		{{ schuelerKaoaManager().getKAOAKategorien() }}
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';

	const props = defineProps<SchuelerKAoAProps>();

</script>
