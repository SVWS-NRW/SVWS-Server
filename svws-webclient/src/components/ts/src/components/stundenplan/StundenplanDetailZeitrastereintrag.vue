<template>
	<svws-ui-content-card>
		<svws-ui-input-wrapper :grid="4">
			<div class="text-headline-md text-left">
				<span>{{ Wochentag.fromIDorException(item.wochentag) }}</span>
			</div>
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @update:model-value="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenende ?? 0)" placeholder="Stundenende" @update:model-value="patchEnde" />
		</svws-ui-input-wrapper>
		<svws-ui-button type="danger" @click="removeZeitraster([item])"> Eintrag entfernen </svws-ui-button>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { DateUtils, Wochentag } from "@core";

	const props = defineProps<{
		item: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	async function patchBeginn(event: string | number) {
		if (typeof event === 'number')
			return;
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchZeitraster({stundenbeginn}, props.item);
	}

	async function patchEnde(event: string | number) {
		if (typeof event === 'number')
			return;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchZeitraster({stundenende}, props.item);
	}


</script>
