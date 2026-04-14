
<template>
	<Story title="QR-Code" id="Qr-Code" icon="ri:cursor-line" :layout="{type: 'grid', width: '45%'}">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:id="title"
			:title>
			<qr-code :uri />
		</Variant>
		<template #controls>
			<HstText v-model="issuerEnc" title="Issuer" />
			<HstText v-model="accountEnc" title="Account" />
			<HstText v-model="secretEnc" title="Secret" />
			<hr>
			<HstText v-model="uri" title="URI" />
		</template>
	</Story>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	const issuerEnc = ref("issuer");
	const accountEnc = ref("account");
	const secretEnc = ref("secret");
	const uri = ref(`otpauth://totp/${issuerEnc.value}:${accountEnc.value}?secret=${secretEnc.value}&issuer=${issuerEnc.value}`);

	const propsVariants = {
		First: { type: "first" },
	} as const;

</script>
