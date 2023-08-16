<template>
	<svws-ui-content-card>
		<svws-ui-text-input :model-value="item" type="number" required placeholder="Stundenbezeichnung" @update:model-value="patchStunde" />
		<svws-ui-button type="danger" @click="removeZeitraster([...props.stundenplanManager().getListZeitrasterZuStunde(props.item)])"> {{ item }}. Stunde entfernen </svws-ui-button>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";

	const props = defineProps<{
		item: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	async function patchStunde(event: string | number) {
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.item))
			await props.patchZeitraster({unterrichtstunde: Number(event)}, zeitraster);
	}
</script>
