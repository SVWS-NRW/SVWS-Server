<template>
	<div class="page--content">
		<svws-ui-todo title="KAoA">
			Dieser Bereich ist noch in Entwicklung. Hier werden die KAoA-Daten abgelegt.
			{{ data() }}
			{{ schuelerKaoaManager().getKAOAKategorien() }}
		</svws-ui-todo>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';

	const props = defineProps<SchuelerKAoAProps>();

</script>
